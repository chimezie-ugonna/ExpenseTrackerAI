package com.expensetrackerai.util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Logger;

public class HttpClient {
    private static final Logger logger = Logger.getLogger(HttpClient.class.getName());

    public static String makePostRequest(String requestUrl, String postData) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(postData.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                try (Scanner responseScanner = new Scanner(connection.getInputStream())) {
                    String responseMessage = responseScanner.hasNext() ? responseScanner.nextLine() : "";
                    return responseMessage.trim();
                }
            } else {
                try (Scanner errorScanner = new Scanner(connection.getErrorStream())) {
                    String errorMessage = errorScanner.hasNext() ? errorScanner.nextLine() : "Error occurred";
                    return "Error: " + errorMessage;
                }
            }
        } catch (IOException e) {
            logger.severe("An error occurred: " + e.getMessage());
            return null;
        }
    }

    public static String makePostRequest2(String requestUrl, String postData) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(postData.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                StringBuilder responseMessage = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseMessage.append(line).append("\n");
                    }
                }
                return responseMessage.toString().trim();
            } else {
                try (Scanner errorScanner = new Scanner(connection.getErrorStream())) {
                    StringBuilder errorMessage = new StringBuilder();
                    while (errorScanner.hasNextLine()) {
                        errorMessage.append(errorScanner.nextLine()).append("\n");
                    }
                    return "Error: " + errorMessage;
                }
            }
        } catch (IOException e) {
            logger.severe("An error occurred: " + e.getMessage());
            return null;
        }
    }

    public static String makePostJsonRequest(String requestUrl, String jsonData) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject payload = new JSONObject();
            payload.put("contents", new JSONObject().put("parts", new JSONObject().put("text", jsonData)));

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString();
                }
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorMessage = new StringBuilder();
                    String errorLine;
                    while ((errorLine = br.readLine()) != null) {
                        errorMessage.append(errorLine.trim());
                    }
                    return "Error: " + errorMessage;
                }
            }
        } catch (Exception e) {
            logger.severe("An error occurred: " + e.getMessage());
            return null;
        }
    }

    public static String makeDeleteRequest(String requestUrl) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");

            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                try (Scanner responseScanner = new Scanner(connection.getInputStream())) {
                    String responseMessage = responseScanner.hasNext() ? responseScanner.nextLine() : "";
                    return responseMessage.trim();
                }
            } else {
                try (Scanner errorScanner = new Scanner(connection.getErrorStream())) {
                    String errorMessage = errorScanner.hasNext() ? errorScanner.nextLine() : "Error occurred";
                    return "Error: " + errorMessage;
                }
            }
        } catch (IOException e) {
            logger.severe("An error occurred: " + e.getMessage());
            return null;
        }
    }

    public static String makeGetRequest(String requestUrl) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                try (Scanner responseScanner = new Scanner(connection.getInputStream())) {
                    String responseMessage = responseScanner.hasNext() ? responseScanner.nextLine() : "";
                    return responseMessage.trim();
                }
            } else {
                try (Scanner errorScanner = new Scanner(connection.getErrorStream())) {
                    String errorMessage = errorScanner.hasNext() ? errorScanner.nextLine() : "Error occurred";
                    return "Error: " + errorMessage;
                }
            }
        } catch (IOException e) {
            logger.severe("An error occurred: " + e.getMessage());
            return null;
        }
    }

    public static String makePutRequest(String requestUrl, String jsonBody) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString();
                }
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorMessage = new StringBuilder();
                    String errorLine;
                    while ((errorLine = br.readLine()) != null) {
                        errorMessage.append(errorLine.trim());
                    }
                    return "Error: " + errorMessage;
                }
            }
        } catch (IOException e) {
            logger.severe("An error occurred: " + e.getMessage());
            return null;
        }
    }
}