package com.expensetrackerai.ui;

import com.expensetrackerai.model.Expense;
import com.expensetrackerai.service.ExpenseService;
import com.expensetrackerai.util.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class DeleteExpenseUi implements UiComponent {

    private static final String BASE_URL = "http://localhost:8080/api/expenses";
    private final ExpenseService expenseService;

    @Autowired
    public DeleteExpenseUi(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Override
    public void start(Scanner scanner, UiManager uiManager) {

    }

    @Override
    public void start(Long userId, Scanner scanner) {
        System.out.println("\n--- Delete Expense ---");

        String expensesJsonResponse = HttpClient.makeGetRequest(BASE_URL + "/read/" + userId);
        if (expensesJsonResponse == null || expensesJsonResponse.isEmpty()) {
            System.out.println("No expenses found to delete.");
            return;
        }

        List<Expense> expenses = expenseService.parseExpensesResponse(expensesJsonResponse);
        if (expenses.isEmpty()) {
            System.out.println("No expenses found to delete.");
            return;
        }

        System.out.println("Select an expense to delete:");
        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            System.out.println((i + 1) + ". " + expense.getAmount() + " EUR | " + expense.getExpenseCategory().getName() + " | " + expense.getDescription() + " | " + expense.getDate());
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
            String deleteResponse = HttpClient.makeDeleteRequest(BASE_URL + "/delete/" + expenseToDelete.getId());
            if (deleteResponse != null && deleteResponse.contains("Expense deleted successfully")) {
                System.out.println("Expense deleted successfully.");
            } else {
                System.out.println("Expense deletion failed. Try again? (yes/no): ");
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

    @Override
    public void start(Long userId, String userFirstName, Scanner scanner, UiManager uiManager) {

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
