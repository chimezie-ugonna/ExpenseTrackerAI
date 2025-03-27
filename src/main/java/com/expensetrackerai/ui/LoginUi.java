package com.expensetrackerai.ui;


import com.expensetrackerai.util.HttpClient;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class LoginUi implements UiComponent {

    private static String[] logIn(String email, String password) {
        String responseMessage = HttpClient.makePostRequest("http://localhost:8080/api/users/login", "email=" + email + "&password=" + password);
        if (responseMessage == null || responseMessage.equals("Invalid credentials!")) {
            return null;
        } else {
            return responseMessage.split(",");
        }
    }

    @Override
    public void start(Scanner scanner, UiManager uiManager) {
        while (true) {
            System.out.print("\nEnter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (email.isEmpty() || password.isEmpty()) {
                System.out.println("All fields are required. Please try again.");
                continue;
            }

            String[] userData = logIn(email, password);
            if (userData != null) {
                String userId = userData[0];
                String fullName = userData[1];
                System.out.println("Login successful.");
                uiManager.startDashboardUi(Long.parseLong(userId), fullName.split(" ")[0], scanner);
            } else {
                String response;
                while (true) {
                    System.out.print("Invalid credentials. Try again? (yes/no): ");
                    response = scanner.nextLine().toLowerCase();
                    if (response.equals("yes") || response.equals("no")) {
                        break;
                    } else {
                        System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                    }
                }
                if (response.equals("no")) {
                    return;
                }
            }
        }
    }

    @Override
    public void start(Long userId, Scanner scanner) {

    }

    @Override
    public void start(Long userId, String userFirstName, Scanner scanner, UiManager uiManager) {

    }
}