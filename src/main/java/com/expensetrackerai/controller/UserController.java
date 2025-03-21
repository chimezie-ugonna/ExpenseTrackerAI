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
    public User registerUser(@RequestParam String email, @RequestParam String password) {
        return userService.createUser(email, password);
    }
}