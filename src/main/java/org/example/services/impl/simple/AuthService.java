package org.example.services.impl.simple;

import lombok.Getter;
import org.example.models.User;
import org.example.repositories.IUserRepository;
import org.example.services.IAuthService;

import java.util.List;
import java.util.Optional;

import static org.mindrot.jbcrypt.BCrypt.*;

@Getter
public class AuthService implements IAuthService {
    private final IUserRepository userRepository;

    public AuthService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> login(String login, String password) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getLogin().equals(login) && checkpw(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public boolean register(String login, String password, String role) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getLogin().equals(login)) {
                return false;
            }
        }
        User newUser = User.builder()
                .id(null)
                .login(login)
                .password(hashpw(password, gensalt()))
                .role(role)
                .build();
        userRepository.save(newUser);
        return true;
    }
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}