package com.expensetrackerai.ui;

import com.expensetrackerai.util.HttpClient;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.regex.Pattern;

@Component
public class RegisterUi implements UiComponent {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Override
    public void start(Scanner scanner, UiManager uiManager) {
        while (true) {
            System.out.print("\nEnter full name: ");
            String fullName = scanner.nextLine().trim();

            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                System.out.println("All fields are required. Please try again.");
                continue;
            }

            if (!isValidFullName(fullName)) {
                System.out.println("Invalid full name. Please enter both first and last names.");
                continue;
            }
            if (!isValidEmail(email)) {
                System.out.println("Invalid email format. Please enter a valid email.");
                continue;
            }
            if (!isValidPassword(password)) {
                System.out.println("Password must be at least 8 characters long.");
                continue;
            }

            String userId = HttpClient.makePostRequest("http://localhost:8080/api/users/register", "fullName=" + fullName + "&email=" + email + "&password=" + password);
            if (userId != null) {
                System.out.println("Registration successful");
                uiManager.startDashboardUi(Long.parseLong(userId), fullName.split(" ")[0], scanner);
            } else {
                String response;
                while (true) {
                    System.out.print("Registration failed. Try again? (yes/no): ");
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

    private boolean isValidFullName(String fullName) {
        return fullName.split(" ").length >= 2;
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }
}