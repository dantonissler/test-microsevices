package br.com.blinkdev.auth.security.filter;

import br.com.blinkdev.core.model.ApplicationUser;
import br.com.blinkdev.core.property.JwtConfiguration;
import br.com.blinkdev.security.token.creator.TokenCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtConfiguration jwtConfiguration;
    private final TokenCreator tokenCreator;

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        log.info("Attempting authentication . . .");
        ApplicationUser applicationUser = new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);
        if (applicationUser == null)
            throw new UsernameNotFoundException("Unable to retrieve the username oor password");
        log.info("Creating the authentication object for the user '{}' and calling UserDatailSerrviceImpl loadUserByUsername", applicationUser);
        UsernamePasswordAuthenticationToken UsernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(applicationUser.getUsername(), applicationUser.getPassword(), Collections.emptyList());
        UsernamePasswordAuthenticationToken.setDetails(applicationUser);
        return authenticationManager.authenticate(UsernamePasswordAuthenticationToken);
    }

    @Override
    @SneakyThrows
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        log.info("Autentication was successful for the user '{}', generationg JWE token", authResult.getName());
        SignedJWT signedJWT = tokenCreator.createSingnedJWT(authResult);
        String encryptToken = tokenCreator.encryptToken(signedJWT);
        log.info("Token generated successfully , adding it to the response header");
        response.addHeader("Access-Control-Expose-Headers", "XSRF-TOKEN" + jwtConfiguration.getHeader().getName());
        response.addHeader(jwtConfiguration.getHeader().getName(), jwtConfiguration.getHeader().getPrefix() + encryptToken);
    }

}
