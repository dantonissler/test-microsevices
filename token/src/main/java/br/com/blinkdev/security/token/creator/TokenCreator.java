package br.com.blinkdev.security.token.creator;

import br.com.blinkdev.core.model.ApplicationUser;
import br.com.blinkdev.core.property.JwtConfiguration;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenCreator {
    private final JwtConfiguration jwtConfiguration;

    @SneakyThrows
    public SignedJWT createSingnedJWT(Authentication auth) {
        log.info("Starting to crreate the singned JWT");
        ApplicationUser applicationUser = (ApplicationUser) auth.getPrincipal();
        JWTClaimsSet jwtClaimsSet = createJWTClaimSet(auth, applicationUser);
        KeyPair rsaKeys = generateKeyPair();
        log.info("Building JWT from the RSA Keys");
        JWK jwk = new RSAKey.Builder((RSAPublicKey) rsaKeys.getPublic()).keyID(UUID.randomUUID().toString()).build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
                .jwk(jwk)
                .type(JOSEObjectType.JWT)
                .build(), jwtClaimsSet);
        log.info("Signing the token with the private RRSA Key");
        RSASSASigner signer = new RSASSASigner(rsaKeys.getPrivate());
        signedJWT.sign(signer);
        return signedJWT;
    }

    private JWTClaimsSet createJWTClaimSet(Authentication auth, ApplicationUser applicationUser) {
        log.info("Creating the JWTClaimSet Object for '{}'", applicationUser);
        return new JWTClaimsSet.Builder()
                .subject(applicationUser.getUsername())
                .claim("authorities", auth.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .claim("userId", applicationUser.getId())
                .issuer("http://br.com.blinkdev")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + (jwtConfiguration.getExpiration() * 1000L)))
                .build();
    }

    @SneakyThrows
    private KeyPair generateKeyPair() {
        log.info("Generating RSA 2048 bits Keys");
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.genKeyPair();
    }

    public String encryptToken(SignedJWT signedJWT) throws JOSEException {
        log.info("Starting the EncryptToken method");
        DirectEncrypter directEncrypter = new DirectEncrypter(jwtConfiguration.getPrivateKey().getBytes());
        JWEObject jweObject = new JWEObject(new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256)
                .contentType("JWT").build(), new Payload(signedJWT));
        log.info("Encrypting token ith system's private key");
        jweObject.encrypt(directEncrypter);
        log.info("Token encrypted");
        return jweObject.serialize();
    }
}
