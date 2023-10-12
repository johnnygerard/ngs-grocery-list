package dev.jgerard.ngsgrocerylist;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

@Configuration
public class JwtConfig {
    private final SecretKey secretKey = makeSymmetricKey();

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
    }

    private SecretKey makeSymmetricKey() {
        final String ALGORITHM = "AES";
        final int KEY_SIZE = 32; // 256 bits
        final var key = new byte[KEY_SIZE];

        new SecureRandom().nextBytes(key); // Fill array with random bytes
        return new SecretKeySpec(key, ALGORITHM);
    }
}
