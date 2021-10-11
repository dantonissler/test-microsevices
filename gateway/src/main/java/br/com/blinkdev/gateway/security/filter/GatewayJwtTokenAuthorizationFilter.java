package br.com.blinkdev.gateway.security.filter;

import br.com.blinkdev.core.property.JwtConfiguration;
import br.com.blinkdev.security.filter.JwtTokenAuthorizationFilter;
import br.com.blinkdev.security.token.converter.TokenConverter;
import com.netflix.zuul.context.RequestContext;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static br.com.blinkdev.security.util.SecurityContextUtil.setSecurityContext;

public class GatewayJwtTokenAuthorizationFilter extends JwtTokenAuthorizationFilter {

    public GatewayJwtTokenAuthorizationFilter(JwtConfiguration jwtConfiguration, TokenConverter tokenConverter) {
        super(jwtConfiguration, tokenConverter);
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("Duplicates")
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        String header = request.getHeader(jwtConfiguration.getHeader().getName());
        if (header == null || !header.startsWith(jwtConfiguration.getHeader().getPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = header.replace(jwtConfiguration.getHeader().getPrefix(), "").trim();
        String signedToken = tokenConverter.decryptToken(token);
        tokenConverter.validateTokenSignature(signedToken);
        setSecurityContext(SignedJWT.parse(signedToken));
        if (jwtConfiguration.getType().equalsIgnoreCase("signed"))
            RequestContext.getCurrentContext().addZuulRequestHeader("Authorization", jwtConfiguration.getHeader().getPrefix() + signedToken);
        filterChain.doFilter(request, response);
    }
}
