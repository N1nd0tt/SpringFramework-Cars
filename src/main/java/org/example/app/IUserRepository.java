package org.example.app;

import java.util.List;

public interface IUserRepository {
    void load();
    List<User> getUsers();
    User getUser(int id);
}
