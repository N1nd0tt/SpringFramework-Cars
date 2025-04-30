package org.example.services.impl.hibernate;

import org.example.models.User;
import org.example.repositories.impl.hibernate.UserHibernateRepository;
import org.example.services.IAuthService;
import org.example.db.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

import static org.mindrot.jbcrypt.BCrypt.*;

public class AuthHibernateService implements IAuthService {
    private final UserHibernateRepository userRepo;

    public AuthHibernateService(UserHibernateRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Optional<User> login(String login, String password) {
        List<User> users = userRepo.findAll();
        for (User user : users) {
            if (user.getLogin().equals(login) && checkpw(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean register(String login, String password, String role) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            userRepo.setSession(session);

            if (userRepo.findAll().stream().anyMatch(user -> user.getLogin().equals(login))) {
                return false;
            }

            User newUser = User.builder()
                    .id(null)
                    .login(login)
                    .password(hashpw(password, gensalt()))
                    .role(role)
                    .build();

            userRepo.save(newUser);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
    @Override
    public List<User> findAllUsers() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            userRepo.setSession(session);
            return userRepo.findAll();
        }
    }
}
