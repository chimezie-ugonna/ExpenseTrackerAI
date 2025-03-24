package com.expensetrackerai.controller;

import com.expensetrackerai.model.User;
import com.expensetrackerai.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String fullName, @RequestParam String email, @RequestParam String password) {
        User newUser = userService.createUser(fullName, email, password);
        return newUser.getId().toString();
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password) {
        boolean isAuthenticated = userService.authenticateUser(email, password);
        if (isAuthenticated) {
            User user = userService.getUserByEmail(email);
            return user.getId() + "," + user.getFullName();
        } else {
            return "Invalid credentials!";
        }
    }
}