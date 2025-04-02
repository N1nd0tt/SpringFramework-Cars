package org.example.repositories.impl.json;

import com.google.gson.reflect.TypeToken;
import org.example.models.User;
import org.example.repositories.IUserRepository;
import org.example.utils.JsonFileStorage;

import java.util.*;

public class UserJsonRepository implements IUserRepository {
    private final JsonFileStorage<User> storage =
            new JsonFileStorage<>("users.json", new TypeToken<List<User>>(){}.getType());
    private final List<User> users;

    public UserJsonRepository() {
        this.users = new
                ArrayList<>(storage.load());
    }
    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public Optional<User> findById(String id) {
        return users.stream().filter(v ->
                v.getId().equals(id)).findFirst();
    }
    @Override
    public Optional<User> findByLogin(String login) {
        return users.stream().filter(v ->
                v.getLogin().equals(login)).findFirst();
    }
    @Override
    public User save(User user) {
        if (user.getId() == null ||
                user.getId().isBlank()) {
            user.setId(UUID.randomUUID().toString());
        } else {
            deleteById(user.getId());
        }
        users.add(user);
        storage.save(users);
        return user;
    }
    @Override
    public void deleteById(String id) {
        users.removeIf(v -> v.getId().equals(id));
        storage.save(users);
    }
}
