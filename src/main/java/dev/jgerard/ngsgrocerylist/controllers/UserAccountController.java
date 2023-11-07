package dev.jgerard.ngsgrocerylist.controllers;

import dev.jgerard.ngsgrocerylist.JwtBuilder;
import dev.jgerard.ngsgrocerylist.entities.User;
import dev.jgerard.ngsgrocerylist.repositories.UserRepository;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserAccountController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtBuilder jwtBuilder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
        @Pattern(regexp = "^\\w{1,32}$") @RequestParam String username,
        @Pattern(regexp = "^[\\x20-\\x7E]{8,256}$") @RequestParam String password
    ) {
        // Create user
        var user = new User();
        user.setUsername(username);

        // Hash and set password
        String hash = passwordEncoder.encode(password);
        user.setPassword(hash);

        // Enforce username uniqueness
        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Username already exists");
        }

        userRepository.save(user);
        return ResponseEntity.ok(jwtBuilder.build(user.getId()).getTokenValue());
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(
        @RequestParam String username,
        @RequestParam String password
    ) {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.ok(jwtBuilder.build(user.getId()).getTokenValue());
        }

        if (user == null) {
            // Hash password to prevent timing attacks
            passwordEncoder.encode(password);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Invalid username or password");
    }
}
