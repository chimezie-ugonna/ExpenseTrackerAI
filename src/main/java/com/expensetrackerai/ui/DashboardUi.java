package com.expensetrackerai.ui;

import java.time.LocalTime;
import java.util.Scanner;

public class DashboardUi {
    public static void start(String userFirstName, Scanner scanner) {
        while (true) {
            String greeting = getGreeting();
            System.out.println("\n" + greeting + ", " + userFirstName + "!");
            System.out.println("What would you like to do today?");

            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Delete Expense");
            System.out.println("4. Get Summary");
            System.out.println("5. Logout");

            System.out.print("Please select an option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    //AddExpenseUi.start(scanner);
                    break;
                case 2:
                    //ViewExpensesUi.start(scanner);
                    break;
                case 3:
                    //DeleteExpenseUi.start(scanner);
                    break;
                case 4:
                    //GetSummaryUi.start(scanner);
                    break;
                case 5:
                    System.out.println("Logout successful");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
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