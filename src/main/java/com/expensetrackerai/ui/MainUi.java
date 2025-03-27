package com.expensetrackerai.ui;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MainUi implements UiComponent {
    @Override
    public void start(Scanner scanner, UiManager uiManager) {
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
                    uiManager.startLoginUi(scanner);
                    break;
                case 2:
                    uiManager.startRegisterUi(scanner);
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

    @Override
    public void start(Long userId, Scanner scanner) {

    }

    @Override
    public void start(Long userId, String userFirstName, Scanner scanner, UiManager uiManager) {

    }
}