package br.com.blinkdev.course.security.config;

import br.com.blinkdev.core.property.JwtConfiguration;
import br.com.blinkdev.security.config.SecurityTokenConfig;
import br.com.blinkdev.security.filter.JwtTokenAuthorizationFilter;
import br.com.blinkdev.security.token.converter.TokenConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
class SecurityCredentialsConfig extends SecurityTokenConfig {
    private final TokenConverter tokenConverter;

    public SecurityCredentialsConfig(JwtConfiguration jwtConfiguration, TokenConverter tokenConverter) {
        super(jwtConfiguration);
        this.tokenConverter = tokenConverter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterAfter(new JwtTokenAuthorizationFilter(jwtConfiguration, tokenConverter), UsernamePasswordAuthenticationFilter.class);
        super.configure(http);
    }
}
