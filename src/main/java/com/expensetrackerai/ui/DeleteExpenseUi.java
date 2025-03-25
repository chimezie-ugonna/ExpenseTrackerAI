package com.expensetrackerai.ui;

import com.expensetrackerai.model.Expense;
import com.expensetrackerai.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class DeleteExpenseUi {

    private final ExpenseService expenseService;

    @Autowired
    public DeleteExpenseUi(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public void start(Long userId, Scanner scanner) {
        System.out.println("\n--- Delete Expense ---");

        List<Expense> expenses = expenseService.getExpensesByUserId(userId);
        if (expenses.isEmpty()) {
            System.out.println("No expenses found to delete.");
            return;
        }

        System.out.println("Select an expense to delete:");
        for (int i = 0; i < expenses.size(); i++) {
            System.out.println((i + 1) + ". " + expenses.get(i).getDescription() + " - €" + expenses.get(i).getAmount());
        }
        System.out.println((expenses.size() + 1) + ". Go Back");

        int expenseIndex;
        while (true) {
            System.out.print("Please select an option: ");
            try {
                expenseIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (expenseIndex == expenses.size()) {
                    return;
                }
                if (expenseIndex < 0 || expenseIndex >= expenses.size()) {
                    System.out.println("Invalid choice. Try again.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number.");
            }
        }

        Expense expenseToDelete = expenses.get(expenseIndex);

        System.out.println("Are you sure you want to delete this expense? This action is irreversible.");
        String confirmation = getConfirmation(scanner);

        if (confirmation.equals("yes")) {
            boolean deletionSuccess = expenseService.deleteExpense(expenseToDelete.getId());

            if (deletionSuccess) {
                System.out.println("Expense deleted successfully.");
            } else {
                System.out.println("Expense deletion failed.");
                String retryResponse = getConfirmation(scanner);

                if (retryResponse.equals("yes")) {
                    start(userId, scanner);
                } else {
                    System.out.println("Expense deletion canceled.");
                }
            }
        } else {
            System.out.println("Expense deletion canceled.");
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
