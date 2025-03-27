package com.expensetrackerai.controller;

import com.expensetrackerai.model.User;
import com.expensetrackerai.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String fullName, @RequestParam String email, @RequestParam String password) {
        try {
            User newUser = userService.createUser(fullName, email, password);
            return ResponseEntity.ok(newUser.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password) {
        try {
            if (userService.authenticateUser(email, password)) {
                User user = userService.getUserByEmail(email);
                if (user != null) {
                    return ResponseEntity.ok(user.getId() + "," + user.getFullName());
                } else {
                    return ResponseEntity.status(401).body("Invalid credentials!");
                }
            } else {
                return ResponseEntity.status(401).body("Invalid credentials!");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Long userId) {
        try {
            boolean deletionSuccess = userService.deleteUserAccount(userId);
            if (deletionSuccess) {
                return ResponseEntity.ok("Account deleted successfully.");
            } else {
                return ResponseEntity.status(400).body("Error: Account deletion failed.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}