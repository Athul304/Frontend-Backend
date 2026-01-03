package com.example.ai_tool_finder.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {}

    public User(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    // getters & setters
    public Long getId() { return id; }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public Role getRole() { return role; }
}
