package org.example.demo1.domain.dao;

import org.example.domain.models.User;

public interface UserDAO {
    User getUserByEmail(String username);
    boolean addUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(int id);
    boolean UserExists(String email);
}
