package com.expensetrackerai.ui;

import com.expensetrackerai.util.HttpClient;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Scanner;

@Component
public class DashboardUi implements UiComponent {

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

    @Override
    public void start(Scanner scanner, UiManager uiManager) {

    }

    @Override
    public void start(Long userId, Scanner scanner) {

    }

    public void start(Long userId, String userFirstName, Scanner scanner, UiManager uiManager) {
        while (true) {
            String greeting = getGreeting();
            System.out.println("\n" + greeting + ", " + userFirstName + "!");
            System.out.println("What would you like to do today?");

            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Update Expense");
            System.out.println("4. Delete Expense");
            System.out.println("5. Get AI Summary");
            System.out.println("6. Manage Expense Categories");
            System.out.println("7. Logout");
            System.out.println("8. Delete Account");

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
                    uiManager.startUpdateExpenseUi(userId, scanner);
                    break;
                case 4:
                    uiManager.startDeleteExpenseUi(userId, scanner);
                    break;
                case 5:
                    System.out.println("Processing...");
                    String aiSummaryResponse = HttpClient.makePostRequest2("http://localhost:8080/api/expenses/aiSummary/" + userId, "");
                    if (aiSummaryResponse != null && !aiSummaryResponse.isEmpty()) {
                        printWithTypingEffect("\n" + aiSummaryResponse, 15);
                    } else {
                        System.out.println("Failed to retrieve AI summary.");
                    }
                    break;
                case 6:
                    uiManager.startManageExpenseCategoriesUi(userId, scanner);
                    break;
                case 7:
                    System.out.println("Logout successful");
                    uiManager.startMainUi(scanner);
                    return;
                case 8:
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
            String response = HttpClient.makeDeleteRequest("http://localhost:8080/api/users/delete?userId=" + userId);

            if (response != null && response.contains("Account deleted successfully")) {
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
}