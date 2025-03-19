package org.example.app;

import java.util.List;

public class Authentication {
    private UserRepository userRepository;

    public Authentication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(String login, String password) {
        List<User> users = userRepository.getUsers();
        for (User user : users) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}