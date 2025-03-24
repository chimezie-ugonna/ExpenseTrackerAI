package com.expensetrackerai.ui;

import com.expensetrackerai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Scanner;

@Component
public class DashboardUi {

    private AddExpenseUi addExpenseUi;
    private UserService userService;

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

    @Autowired
    public void setAddExpenseUi(AddExpenseUi addExpenseUi) {
        this.addExpenseUi = addExpenseUi;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
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
                    addExpenseUi.start(scanner, userId);
                    break;
                case 2:
                    System.out.println("View Expenses option selected.");
                    break;
                case 3:
                    System.out.println("Delete Expense option selected.");
                    break;
                case 4:
                    System.out.println("AI Summary option selected.");
                    break;
                case 5:
                    System.out.println("Manage Expense Categories option selected.");
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
                    return;
                } else if (retryResponse.equals("yes")) {
                    deleteAccountFlow(userId, scanner, uiManager);
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                    deleteAccountFlow(userId, scanner, uiManager);
                }
            }
        } else if (confirmation.equals("no")) {
            System.out.println("Account deletion canceled.");
            return;
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