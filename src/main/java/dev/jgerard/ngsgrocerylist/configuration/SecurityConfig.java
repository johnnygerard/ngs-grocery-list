package dev.jgerard.ngsgrocerylist.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SecurityConfig {
    // This bean is required to fix CVE-2023-34035.
    // https://spring.io/security/cve-2023-34035
    @Bean
    public MvcRequestMatcher.Builder mvcBuilder(ApplicationContext context) {
        var introspector = new HandlerMappingIntrospector();
        introspector.setApplicationContext(context);

        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        JwtDecoder jwtDecoder,
        MvcRequestMatcher.Builder mvcBuilder
    ) throws Exception {
        return http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(mvcBuilder.pattern(HttpMethod.POST, "/api/login")).permitAll()
                .requestMatchers(mvcBuilder.pattern(HttpMethod.POST, "/api/register")).permitAll()
                .requestMatchers(mvcBuilder.pattern("/api/**")).authenticated()
                .anyRequest().permitAll()
            )
            .csrf(AbstractHttpConfigurer::disable)
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                jwt -> jwt.decoder(jwtDecoder))
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .build();
    }
}
