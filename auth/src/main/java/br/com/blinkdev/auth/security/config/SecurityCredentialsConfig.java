package br.com.blinkdev.auth.security.config;

import br.com.blinkdev.auth.security.filter.JwtUsernameAndPasswordAuthenticationFilter;
import br.com.blinkdev.core.property.JwtConfiguration;
import br.com.blinkdev.security.config.SecurityTokenConfig;
import br.com.blinkdev.security.filter.JwtTokenAuthorizationFilter;
import br.com.blinkdev.security.token.converter.TokenConverter;
import br.com.blinkdev.security.token.creator.TokenCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityCredentialsConfig extends SecurityTokenConfig {
    private final UserDetailsService userDatailsService;
    private final TokenCreator tokenCreator;
    private final TokenConverter tokenConverter;

    public SecurityCredentialsConfig(JwtConfiguration jwtConfiguration, UserDetailsService userDatailsService, TokenCreator tokenCreator, TokenConverter tokenConverter) {
        super(jwtConfiguration);
        this.userDatailsService = userDatailsService;
        this.tokenCreator = tokenCreator;
        this.tokenConverter = tokenConverter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfiguration, tokenCreator))
                .addFilterAfter(new JwtTokenAuthorizationFilter(jwtConfiguration, tokenConverter), UsernamePasswordAuthenticationFilter.class);
        super.configure(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDatailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
