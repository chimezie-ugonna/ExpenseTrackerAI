package com.expensetrackerai.ui;


import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class LoginUi {
    public static void start(Scanner scanner) {
        while (true) {
            System.out.print("\nEnter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (email.isEmpty() || password.isEmpty()) {
                System.out.println("All fields are required. Please try again.");
                continue;
            }

            String fullName = logIn(email, password);
            if (fullName != null) {
                System.out.println("Login successful");
                DashboardUi.start(fullName.split(" ")[0], scanner);
                return;
            } else {
                System.out.println("Invalid credentials. Try again? (yes/no)");
                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                    return;
                }
            }
        }
    }

    private static String logIn(String email, String password) {
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
                return responseMessage.trim();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}