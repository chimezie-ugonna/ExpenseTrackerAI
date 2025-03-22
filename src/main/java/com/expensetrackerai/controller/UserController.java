package com.expensetrackerai.controller;

import com.expensetrackerai.model.User;
import com.expensetrackerai.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestParam String fullName, @RequestParam String email, @RequestParam String password) {
        return userService.createUser(fullName, email, password);
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password) {
        boolean isAuthenticated = userService.authenticateUser(email, password);
        if (isAuthenticated) {
            User user = userService.getUserByEmail(email);
            return user.getFullName();
        } else {
            return "Invalid credentials!";
        }
    }
}