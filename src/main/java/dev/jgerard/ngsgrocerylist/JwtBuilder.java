package dev.jgerard.ngsgrocerylist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtBuilder {
    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private MacAlgorithm jwsAlgorithm;

    public Jwt build(String username) {
        var now = Instant.now();
        var header = JwsHeader.with(jwsAlgorithm).build();
        var claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(3600))
            .subject(username)
            .build();
        var parameters = JwtEncoderParameters.from(header, claims);

        return jwtEncoder.encode(parameters);
    }
}
