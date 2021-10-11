package br.com.blinkdev.auth.endpoint.services;

import br.com.blinkdev.core.model.ApplicationUser;
import br.com.blinkdev.core.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationUserServiceImpl implements ApplicationUserService {
    private final ApplicationUserRepository applicationUserrRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public ApplicationUser save(ApplicationUser usuario) {
        log.info("UserService - save");
        if (applicationUserrRepository.findByUsername(usuario.getUsername()).isPresent())
            throw new UsernameNotFoundException("This username is already registered.");
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return applicationUserrRepository.save(usuario);
    }
}
