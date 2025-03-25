package com.expensetrackerai.ui;

import com.expensetrackerai.model.Expense;
import com.expensetrackerai.service.ExpenseService;
import com.expensetrackerai.service.UserService;
import com.expensetrackerai.util.HttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

@Component
public class DashboardUi {

    private static final String API_KEY = "Your API Key";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;
    private UserService userService;
    private ExpenseService expenseService;

    private static String getGreeting() {
        int hour = LocalTime.now().getHour();
        if (hour >= 5 && hour < 12) {
            return "Good morning";
        } else if (hour >= 12 && hour < 17) {
            return "Good afternoon";
        } else if (hour >= 17 && hour < 21) {
            return "Good evening";
        } else {
            return "Good night";
        }
    }

    public static void printWithTypingEffect(String text, int delay) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }

    private static StringBuilder getStringBuilder(List<Expense> expenses) {
        StringBuilder expenseList = new StringBuilder();

        for (Expense expense : expenses) {
            expenseList.append(String.format(
                    "Amount: %.2f, Category: %s, Description: %s, Date: %s\n",
                    expense.getAmount(),
                    expense.getExpenseCategory().getCategory_name(),
                    expense.getDescription(),
                    expense.getDate().toString()
            ));
        }
        return expenseList;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setExpenseService(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public void start(Long userId, String userFirstName, Scanner scanner, UiManager uiManager) {
        while (true) {
            String greeting = getGreeting();
            System.out.println("\n" + greeting + ", " + userFirstName + "!");
            System.out.println("What would you like to do today?");

            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Delete Expense");
            System.out.println("4. Get AI Summary");
            System.out.println("5. Manage Expense Categories");
            System.out.println("6. Logout");
            System.out.println("7. Delete Account");

            System.out.print("Please select an option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    uiManager.startAddExpenseUi(userId, scanner);
                    break;
                case 2:
                    uiManager.startViewExpensesUi(userId, scanner);
                    break;
                case 3:
                    uiManager.startDeleteExpenseUi(userId, scanner);
                    break;
                case 4:
                    getAISummary(userId);
                    break;
                case 5:
                    uiManager.startManageExpenseCategoriesUi(userId, scanner);
                    break;
                case 6:
                    System.out.println("Logout successful");
                    uiManager.startMainUi(scanner);
                    return;
                case 7:
                    deleteAccountFlow(userId, scanner, uiManager);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }
    }

    private void deleteAccountFlow(Long userId, Scanner scanner, UiManager uiManager) {
        System.out.println("Are you sure you want to delete your account? This action is irreversible.");
        String confirmation = getConfirmation(scanner);

        if (confirmation.equals("yes")) {
            boolean deletionSuccess = userService.deleteUserAccount(userId);

            if (deletionSuccess) {
                System.out.println("Your account has been deleted.");
                uiManager.startMainUi(scanner);
            } else {
                System.out.print("Account deletion failed. Try again? (yes/no): ");
                String retryResponse = scanner.nextLine().toLowerCase();

                if (retryResponse.equals("no")) {
                    System.out.println("Account deletion canceled.");
                } else if (retryResponse.equals("yes")) {
                    deleteAccountFlow(userId, scanner, uiManager);
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                    deleteAccountFlow(userId, scanner, uiManager);
                }
            }
        } else if (confirmation.equals("no")) {
            System.out.println("Account deletion canceled.");
        } else {
            System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            deleteAccountFlow(userId, scanner, uiManager);
        }
    }

    private String getConfirmation(Scanner scanner) {
        while (true) {
            System.out.print("Type 'yes' to confirm or 'no' to cancel: ");
            String confirmation = scanner.nextLine().toLowerCase();
            if (confirmation.equals("yes") || confirmation.equals("no")) {
                return confirmation;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }
    }

    private void getAISummary(Long userId) {
        List<Expense> expenses = expenseService.getExpensesByUserId(userId);
        String jsonData = generateJsonData(expenses);

        System.out.println("Processing...");

        String aiSummary = HttpClient.makePostJsonRequest(API_URL, jsonData);

        if (aiSummary != null) {
            String formattedSummary = extractAISummary(aiSummary);
            printWithTypingEffect("\n" + formattedSummary, 15);
        } else {
            System.out.println("Failed to retrieve AI summary.");
        }
    }

    private String extractAISummary(String jsonResponse) {
        try {
            JSONObject responseJson = new JSONObject(jsonResponse);
            JSONArray candidates = responseJson.getJSONArray("candidates");

            if (!candidates.isEmpty()) {
                JSONObject firstCandidate = candidates.getJSONObject(0);
                JSONObject content = firstCandidate.getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");

                StringBuilder summary = new StringBuilder();
                for (int i = 0; i < parts.length(); i++) {
                    summary.append(parts.getJSONObject(i).getString("text")).append("\n\n");
                }

                return summary.toString().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing AI summary.";
        }
        return "No AI summary available.";
    }

    private String generateJsonData(List<Expense> expenses) {
        StringBuilder expenseList = getStringBuilder(expenses);

        String prompt = "You are an AI assistant for an Expense Tracker application. Your task is to analyze the following expense data and generate a detailed, structured, user-friendly financial summary. " +
                "Do not use asterisks or markdown formatting—only provide clear, well-structured plain text. " +
                "Your summary should include:\n" +
                "- Total spending (overall and categorized breakdown)\n" +
                "- Top expense categories (with percentage of total spending)\n" +
                "- Unusual transactions (highlight outliers or anomalies, including any significant or unexpected transactions)\n" +
                "- Spending trends (include any notable patterns over time, like increasing or decreasing spending in specific categories)\n" +
                "- Comparative analysis (compare this period with the previous period, highlighting any changes in spending habits)\n" +
                "- Savings opportunities (suggest areas where spending can be reduced)\n" +
                "- Budgeting advice (suggest budgeting strategies or allocation of funds for the future)\n" +
                "- Recommendations for financial goals (e.g., reducing debt, increasing savings)\n" +
                "- Financial health score (optional, if possible, provide a health score or an assessment of the user's financial situation)\n" +
                "- Key insights (any other noteworthy trends, such as recurring expenses that could be reduced or avoided)\n\n" +
                "Expense Data in Euro:\n" + expenseList;

        JSONObject textPart = new JSONObject();
        textPart.put("text", prompt);

        JSONObject partsObject = new JSONObject();
        partsObject.put("parts", new JSONArray().put(textPart));

        JSONArray contentsArray = new JSONArray();
        contentsArray.put(partsObject);

        JSONObject finalJson = new JSONObject();
        finalJson.put("contents", contentsArray);

        return finalJson.toString();
    }
}