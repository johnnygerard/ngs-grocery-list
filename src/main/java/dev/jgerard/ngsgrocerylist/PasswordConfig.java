package dev.jgerard.ngsgrocerylist;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Default value (should be tweaked for production)
        // An ideal strength value is one that takes about 1 second to hash the password
        final int STRENGTH = 10;
        return new BCryptPasswordEncoder(STRENGTH);
    }
}
