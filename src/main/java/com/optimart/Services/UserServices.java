package com.optimart.Services;

import com.optimart.DataStructures.HashMap;
import com.optimart.Models.User;
import com.optimart.Utils.FileHandler;
import com.optimart.Utils.PassUtil;

import java.util.ArrayList;
import java.util.List;

public class UserServices {
    private HashMap<String, User> userMap;
    private User currentUser;

    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";
    private static final String DEFAULT_ADMIN_EMAIL = "admin@ecommerce.com";

    public UserServices() {
        userMap = new HashMap<>();
        loadUsers();
        createDefaultAdminIfNotExists();
    }

    private void createDefaultAdminIfNotExists() {
        if (userMap.containsKey(DEFAULT_ADMIN_USERNAME)) {
            return;
        }

        boolean hasAdmin = false;
        for (String username : userMap.keySet()) {
            User user = userMap.get(username);
            if ("admin".equalsIgnoreCase(user.getRole())) {
                hasAdmin = true;
                break;
            }
        }

        if (!hasAdmin) {
            User defaultAdmin = new User(DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD,
                    DEFAULT_ADMIN_EMAIL, "admin");
            userMap.put(DEFAULT_ADMIN_USERNAME, defaultAdmin);
            saveUsers();
            System.out.println("✓ Default admin account created");
            System.out.println("  Username: " + DEFAULT_ADMIN_USERNAME);
            System.out.println("  Password: " + DEFAULT_ADMIN_PASSWORD);
            System.out.println("  Please change the password after first login!");
        }
    }

    private void loadUsers() {
        List<User> users = FileHandler.loadUsers();
        for (User user : users) {
            userMap.put(user.getUsername(), user);
        }
        System.out.println("✓ Loaded " + users.size() + " users");
    }

    private void saveUsers() {
        List<User> users = new ArrayList<>();
        for (String username : userMap.keySet()) {
            users.add(userMap.get(username));
        }
        FileHandler.saveUsers(users);
    }

    public User signup(String username, String password, String email) {
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            System.out.println("✗ Username cannot be empty");
            return null;
        }

        if (password == null || !PassUtil.isValidPassword(password)) {
            System.out.println("✗ " + PassUtil.getPasswordStrengthMessage(password));
            return null;
        }

        if (userMap.containsKey(username)) {
            System.out.println("✗ Username already exists");
            return null;
        }

        String role = "customer";

        User newUser = new User(username, password, email, role);

        userMap.put(username, newUser);
        saveUsers();

        System.out.println("✓ Account registered successfully!");

        currentUser = newUser;
        System.out.println("✓ You are now logged in!");

        return newUser;
    }

    public boolean login(String username, String password) {
        if (!userMap.containsKey(username)) {
            System.out.println("✗ Invalid username or password");
            return false;
        }

        User user = userMap.get(username);

        if (PassUtil.verifyPassword(password, user.getPassword())) {
            currentUser = user;
            System.out.println("✓ Login successful! Welcome " + username);
            return true;
        } else {
            System.out.println("✗ Invalid username or password");
            return false;
        }
    }


    public void logout() {
        if (currentUser != null) {
            System.out.println("✓ Logged out successfully. Goodbye " + currentUser.getUsername());
            currentUser = null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole());
    }

    public boolean updateProfile(String email) {
        if (currentUser == null) {
            System.out.println("✗ Please login first");
            return false;
        }

        currentUser.setEmail(email);
        userMap.put(currentUser.getUsername(), currentUser);
        saveUsers();

        System.out.println("✓ Profile updated successfully");
        return true;
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (currentUser == null) {
            System.out.println("✗ Please login first");
            return false;
        }

        if (!PassUtil.verifyPassword(oldPassword, currentUser.getPassword())) {
            System.out.println("✗ Incorrect old password");
            return false;
        }

        if (!PassUtil.isValidPassword(newPassword)) {
            System.out.println("✗ " + PassUtil.getPasswordStrengthMessage(newPassword));
            return false;
        }

        currentUser.setPassword(newPassword);
        userMap.put(currentUser.getUsername(), currentUser);
        saveUsers();

        System.out.println("✓ Password changed successfully");
        return true;
    }

    public boolean promoteToAdmin(String username) {
        if (!isAdmin()) {
            System.out.println("✗ Only admins can promote users");
            return false;
        }

        if (!userMap.containsKey(username)) {
            System.out.println("✗ User not found");
            return false;
        }

        User user = userMap.get(username);

        if ("admin".equalsIgnoreCase(user.getRole())) {
            System.out.println("✗ User is already an admin");
            return false;
        }

        user.setRole("admin");
        userMap.put(username, user);
        saveUsers();

        System.out.println("✓ User '" + username + "' promoted to admin");
        return true;
    }

    public boolean demoteToCustomer(String username) {
        if (!isAdmin()) {
            System.out.println("✗ Only admins can demote users");
            return false;
        }

        if (!userMap.containsKey(username)) {
            System.out.println("✗ User not found");
            return false;
        }

        User user = userMap.get(username);

        if ("customer".equalsIgnoreCase(user.getRole())) {
            System.out.println("✗ User is already a customer");
            return false;
        }

        // Prevent demoting yourself
        if (currentUser != null && currentUser.getUsername().equals(username)) {
            System.out.println("✗ You cannot demote yourself");
            return false;
        }

        user.setRole("customer");
        userMap.put(username, user);
        saveUsers();

        System.out.println("✓ User '" + username + "' demoted to customer");
        return true;
    }

    public List<User> getAllUsers() {
        if (!isAdmin()) {
            System.out.println("✗ Admin access required");
            return new ArrayList<>();
        }

        List<User> users = new ArrayList<>();
        for (String username : userMap.keySet()) {
            users.add(userMap.get(username));
        }
        return users;
    }

    public void displayStats() {
        System.out.println("\n=== User Service Statistics ===");
        System.out.println("Total users: " + userMap.size());
        userMap.displayStats();
    }
}
