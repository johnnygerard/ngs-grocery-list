package dev.jgerard.ngsgrocerylist.configuration;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

@Configuration
public class JwtConfig {
    // Symmetric key for signing and verifying JWTs
    private final SecretKey secretKey;
    private final MacAlgorithm jwsAlgorithm = MacAlgorithm.HS256;

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
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
            .withSecretKey(secretKey)
            .macAlgorithm(jwsAlgorithm)
            .build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
    }

    public String generateJwt(String username) {
        var now = Instant.now();
        var header = JwsHeader.with(jwsAlgorithm).build();
        var claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(3600))
            .subject(username)
            .build();
        var parameters = JwtEncoderParameters.from(header, claims);

        return jwtEncoder().encode(parameters).getTokenValue();
    }
}
