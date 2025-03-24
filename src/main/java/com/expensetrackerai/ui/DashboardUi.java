package com.expensetrackerai.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Scanner;

@Component
public class DashboardUi {

    private AddExpenseUi addExpenseUi;

    @Autowired
    public void setAddExpenseUi(AddExpenseUi addExpenseUi) {
        this.addExpenseUi = addExpenseUi;
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
                    System.out.println("Delete Account option selected.");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }
    }

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
}