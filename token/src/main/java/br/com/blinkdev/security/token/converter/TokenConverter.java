package br.com.blinkdev.security.token.converter;

import br.com.blinkdev.core.property.JwtConfiguration;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;


@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenConverter {
    private final JwtConfiguration jwtConfiguration;

    @SneakyThrows
    public String decryptToken(String encryptedToken) {
        log.info("Decripting token");
        JWEObject jweObject = JWEObject.parse(encryptedToken);
        DirectDecrypter directDecrypter = new DirectDecrypter(jwtConfiguration.getPrivateKey().getBytes());
        jweObject.decrypt(directDecrypter);
        log.info("Token decrypted, returning signed token . . .");
        return jweObject.getPayload().toSignedJWT().serialize();
    }

    @SneakyThrows
    public void validateTokenSignature(String signedToken) {
        log.info("Starting method to validate token signature . . .");
        SignedJWT signedJWT = SignedJWT.parse(signedToken);
        log.info("Token Parsed! Retrieving public key from singed token");
        RSAKey publicKey = RSAKey.parse(signedJWT.getHeader().getJWK().toJSONObject());
        log.info("Public key retrrived, validating signature");
        if(!signedJWT.verify(new RSASSAVerifier(publicKey)))
            throw new AccessDeniedException("Invalid token signature");
        log.info("The token has a valid signature");
    }
}
