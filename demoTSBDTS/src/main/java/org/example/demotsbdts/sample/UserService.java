package org.example.demotsbdts.sample;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UserService {
    private final List<User> users = new ArrayList<>();

    public boolean login(String email, String password) {
        if (email == null || password == null) {
            return false;
        }

        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }

    public void registerUser(String name, String email, String password) {
        if (name == null || email == null || password == null) {
            throw new IllegalArgumentException("User data cannot be null");
        }

        User user = new User(name, email, password);
        users.add(user);
    }

    public boolean isEmailAlreadyUsed(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }

        return false;
    }

    public List<User> sortUsersByName() {
        List<User> sortedUsers = new ArrayList<>(users);
        sortedUsers.sort(Comparator.comparing(User::getName));
        return sortedUsers;
    }

    public void resetPassword(String email, String newPassword) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                user.setPassword(newPassword);
                return;
            }
        }

        throw new IllegalArgumentException("User not found");
    }

    public void deleteUserByEmail(String email) {
        users.removeIf(user -> user.getEmail().equals(email));
    }
}
