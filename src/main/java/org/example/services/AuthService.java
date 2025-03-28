package org.example.services;

import org.example.models.User;
import org.example.repositories.impl.UserJsonRepository;

import java.util.List;
import java.util.Optional;

import static org.mindrot.jbcrypt.BCrypt.*;

public class AuthService {
    private UserJsonRepository userJsonRepository;

    public AuthService(UserJsonRepository userJsonRepository) {
        this.userJsonRepository = userJsonRepository;
    }

    //Login add hash
    public Optional<User> login(String login, String password) {
        List<User> users = userJsonRepository.findAll();
        for (User user : users) {
            if (user.getLogin().equals(login) && checkpw(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public boolean register(String login, String password) {
        List<User> users = userJsonRepository.findAll();
        for (User user : users) {
            if (user.getLogin().equals(login)) {
                return false;
            }
        }
        User newUser = User.builder()
                .id(null)
                .login(login)
                .password(hashpw(password, gensalt()))
                .role("USER")
                .build();
        userJsonRepository.save(newUser);
        return true;
    }
}