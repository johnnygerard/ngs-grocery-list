package dev.jgerard.ngsgrocerylist.configuration;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

@Configuration
public class JwtConfig {
    // Symmetric key for signing and verifying JWTs
    private final SecretKey secretKey;

    public JwtConfig() {
        final String ALGORITHM = "HmacSHA256";
        final int KEY_BIT_SIZE = 256;
        KeyGenerator keyGenerator;

        try {
            keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            var message = "No Provider supports a KeyGeneratorSpi implementation for %s";
            throw new RuntimeException(message.formatted(ALGORITHM), e);
        }

        keyGenerator.init(KEY_BIT_SIZE);
        secretKey = keyGenerator.generateKey();
    }

    @Bean
    public MacAlgorithm jwsAlgorithm() {
        return MacAlgorithm.HS256;
    }

    @Bean
    public JwtDecoder jwtDecoder(MacAlgorithm jwsAlgorithm) {
        return NimbusJwtDecoder
            .withSecretKey(secretKey)
            .macAlgorithm(jwsAlgorithm)
            .build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
    }
}
