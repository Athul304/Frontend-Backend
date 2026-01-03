
// src/main/java/com/example/ai_tool_finder/controller/UserController.java
package com.example.ai_tool_finder.controller;

import com.example.ai_tool_finder.model.Role;
import com.example.ai_tool_finder.model.User;
import com.example.ai_tool_finder.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    // Inject repository via constructor
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Simple DTO for login request body
    public static class LoginRequest {
        public String username;
        public String password;
    }

    /**
     * Validate user by username and password (plaintext comparison).
     * Returns role-based message if credentials are valid.
     *
     * POST /user/login
     * Body: { "username": "john_doe", "password": "password123" }
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest req) {
        if (req == null || req.username == null || req.password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username and password are required");
        }

        Optional<User> userOpt = userRepository.findByUsername(req.username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        User user = userOpt.get();

        // Plaintext comparison — for demo only
        if (!req.password.equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }

        String message = switch (user.getRole()) {
            case ADMIN -> "Admin access granted";
            case USER  -> "User access granted";
        };

        return ResponseEntity.ok(message);
    }
}
