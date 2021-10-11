package br.com.blinkdev.security.util;

import br.com.blinkdev.core.model.ApplicationUser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SecurityContextUtil {
    private SecurityContextUtil() {

    }

    public static void setSecurityContext(SignedJWT signedJWT) {
        try {
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            String username = claims.getSubject();
            if (username == null)
                throw new JOSEException("Username missing from JWT");
            List<String> authorities = claims.getStringListClaim("authorities");
            ApplicationUser applicationUser = ApplicationUser.builder()
                    .id(claims.getLongClaim("userId"))
                    .username(username)
                    .role(String.join(",", authorities))
                    .build();
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(applicationUser, null, createAuthories(authorities));
            auth.setDetails(signedJWT.serialize());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            log.error("Errorr setting securrity coontext ", e);
            SecurityContextHolder.clearContext();
        }
    }

    private static List<SimpleGrantedAuthority> createAuthories(List<String> authorities) {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
