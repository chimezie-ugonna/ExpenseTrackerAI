package com.expensetrackerai.ui;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class RegisterUi {
    public static void start(Scanner scanner) {
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

            boolean success = registerUser(fullName, email, password);
            if (success) {
                System.out.println("Registration successful");
                DashboardUi.start(fullName.split(" ")[0], scanner);
                return;
            } else {
                System.out.println("Registration failed. Try again? (yes/no)");
                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                    return;
                }
            }
        }
    }

    private static boolean registerUser(String fullName, String email, String password) {
        try {
            URL url = new URL("http://localhost:8080/api/users/register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String postData = "fullName=" + fullName + "&email=" + email + "&password=" + password;
            try (OutputStream os = conn.getOutputStream()) {
                os.write(postData.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            return responseCode == 200 || responseCode == 201;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}