package dev.jgerard.ngsgrocerylist.controllers;

import dev.jgerard.ngsgrocerylist.configuration.JwtConfig;
import dev.jgerard.ngsgrocerylist.entities.User;
import dev.jgerard.ngsgrocerylist.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserAccountController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(
        @RequestBody @Valid User user,
        JwtConfig jwtConfig
    ) {
        String username = user.getUsername();
        String password = user.getPassword();
        user.setId(null); // Enforce ID auto-generation

        // Validate password (8-256 printable ASCII characters)
        String regex = "^[\\x20-\\x7E]{8,256}$";
        if (!password.matches(regex)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Malformed password (regex: %s)".formatted(regex));
        }

        // Hash and set password
        String hash = passwordEncoder.encode(password);
        user.setPassword(hash);

        // Enforce username uniqueness
        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Username already exists");
        }

        userRepository.save(user);
        return ResponseEntity.ok(jwtConfig.generateJwt(username));
    }
}
