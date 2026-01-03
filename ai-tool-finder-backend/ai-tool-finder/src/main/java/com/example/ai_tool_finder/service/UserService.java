package com.example.ai_tool_finder.service;

import com.example.ai_tool_finder.model.User;
import com.example.ai_tool_finder.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getAdminUser() {
        return userRepository.findByUsername("admin")
                .orElseThrow(() -> new RuntimeException("Admin user not found"));
    }

    public User getNormalUser() {
        return userRepository.findByUsername("user")
                .orElseThrow(() -> new RuntimeException("Normal user not found"));
    }
}
