package br.com.blinkdev.auth.security.user;

import br.com.blinkdev.core.model.ApplicationUser;
import br.com.blinkdev.core.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailsServiceimpl implements UserDetailsService {

    private final ApplicationUserRepository applicationUserrRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("Searching in the Db the user by username '{}'", username);
        Optional<ApplicationUser> applicationUser = applicationUserrRepository.findByUsername(username);
        log.info("ApplicationUser found '{}'", username);
        if (applicationUser.isEmpty())
            throw new UsernameNotFoundException(String.format("Application user '%s' not found", username));
        return new CustomUserDatails(applicationUser.get());
    }

    private static final class CustomUserDatails extends ApplicationUser implements UserDetails {
        CustomUserDatails(ApplicationUser applicationUser) {
            super(applicationUser);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + this.getRole());
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}


