package com.expensetrackerai.ui;

import com.expensetrackerai.model.Expense;
import com.expensetrackerai.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

@Component
public class ViewExpensesUi {

    private final ExpenseService expenseService;

    @Autowired
    public ViewExpensesUi(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public void start(Scanner scanner, Long userId) {
        while (true) {
            System.out.println("\n--- View Expenses ---");

            List<Expense> expenses = expenseService.getExpensesByUserId(userId);
            if (expenses.isEmpty()) {
                System.out.println("No expenses found for this user.");
                return;
            }

            System.out.println("How would you like to view your expenses?");
            System.out.println("1. Sort by Date (Newest to Oldest)");
            System.out.println("2. Sort by Date (Oldest to Newest)");
            System.out.println("3. Sort by Amount (Highest to Lowest)");
            System.out.println("4. Sort by Amount (Lowest to Highest)");
            System.out.println("5. Go Back");

            System.out.print("Please select an option: ");
            int choice = getValidChoice(scanner);

            switch (choice) {
                case 1:
                    expenses.sort((e1, e2) -> e2.getDate().compareTo(e1.getDate()));
                    break;
                case 2:
                    expenses.sort(Comparator.comparing(Expense::getDate));
                    break;
                case 3:
                    expenses.sort((e1, e2) -> Double.compare(e2.getAmount(), e1.getAmount()));
                    break;
                case 4:
                    expenses.sort(Comparator.comparingDouble(Expense::getAmount));
                    break;
                case 5:
                    return;
            }

            displayExpenses(expenses);

            String viewAgain;
            while (true) {
                System.out.print("\nDo you want to view more? (yes/no): ");
                viewAgain = scanner.nextLine().trim().toLowerCase();
                if (viewAgain.equals("yes") || viewAgain.equals("no")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }
            if (viewAgain.equals("no")) {
                return;
            }
        }
    }

    private int getValidChoice(Scanner scanner) {
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= 5) {
                    break;
                } else {
                    System.out.print("Invalid choice. Please select a valid option: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
        return choice;
    }

    private void displayExpenses(List<Expense> expenses) {
        System.out.println("\n--- Your Expenses ---");
        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            System.out.println((i + 1) + ". " + expense.getAmount() + " EUR | " + expense.getExpenseCategory().getCategory_name() + " | " + expense.getDescription() + " | " + expense.getDate());
        }
    }
}