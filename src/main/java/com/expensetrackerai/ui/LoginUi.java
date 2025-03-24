package com.expensetrackerai.ui;


import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Component
public class LoginUi {

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

    private static String[] logIn(String email, String password) {
        try {
            URL url = new URL("http://localhost:8080/api/users/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String postData = "email=" + email + "&password=" + password;
            try (OutputStream os = conn.getOutputStream()) {
                os.write(postData.getBytes(StandardCharsets.UTF_8));
            }

            Scanner responseScanner = new Scanner(conn.getInputStream());
            String responseMessage = responseScanner.hasNext() ? responseScanner.nextLine() : "";

            if (responseMessage.equals("Invalid credentials!")) {
                return null;
            } else {
                return responseMessage.split(",");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}