package com.expensetrackerai.ui;

import java.util.Scanner;

public class MainUi {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWelcome to Expense Tracker AI!");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");

            System.out.print("Please select an option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter 1, 2, or 3.");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    LoginUi.start(scanner);
                    break;
                case 2:
                    RegisterUi.start(scanner);
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }
}