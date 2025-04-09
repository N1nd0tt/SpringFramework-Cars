package org.example.services;

import org.example.models.User;

import java.util.Optional;

public interface IAuthService {
    boolean register(String login, String rawPassword, String role);
    Optional<User> login(String login, String rawPassword);
}
