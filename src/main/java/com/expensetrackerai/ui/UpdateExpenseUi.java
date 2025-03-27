package com.expensetrackerai.ui;

import com.expensetrackerai.model.Expense;
import com.expensetrackerai.model.ExpenseCategory;
import com.expensetrackerai.service.ExpenseCategoryService;
import com.expensetrackerai.service.ExpenseService;
import com.expensetrackerai.util.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Component
public class UpdateExpenseUi implements UiComponent {

    private static final String BASE_URL = "http://localhost:8080/api/expenses";
    private final ExpenseService expenseService;
    private final ExpenseCategoryService expenseCategoryService;

    @Autowired
    public UpdateExpenseUi(ExpenseService expenseService, ExpenseCategoryService expenseCategoryService) {
        this.expenseService = expenseService;
        this.expenseCategoryService = expenseCategoryService;
    }

    @Override
    public void start(Scanner scanner, UiManager uiManager) {

    }

    @Override
    public void start(Long userId, Scanner scanner) {
        while (true) {
            System.out.println("\n--- Update Expense ---");

            String expensesJsonResponse = HttpClient.makeGetRequest(BASE_URL + "/read/" + userId);
            if (expensesJsonResponse == null || expensesJsonResponse.isEmpty()) {
                System.out.println("No expenses found to update.");
                return;
            }

            List<Expense> expenses = expenseService.parseExpensesResponse(expensesJsonResponse);
            if (expenses.isEmpty()) {
                System.out.println("No expenses found to update.");
                return;
            }

            System.out.println("Select an expense to update:");
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

            Expense selectedExpense = expenses.get(expenseIndex);

            while (true) {
                System.out.print("Enter new amount (or press Enter to keep €" + selectedExpense.getAmount() + "): ");
                String amountInput = scanner.nextLine().trim();
                if (amountInput.isEmpty()) {
                    break;
                }
                try {
                    selectedExpense.setAmount(Double.parseDouble(amountInput));
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount.");
                }
            }

            System.out.print("Enter new description (or press Enter to keep '" + selectedExpense.getDescription() + "'): ");
            String descriptionInput = scanner.nextLine().trim();
            if (!descriptionInput.isEmpty()) {
                selectedExpense.setDescription(descriptionInput);
            }

            while (true) {
                System.out.print("Enter new date (yyyy-mm-dd) or press Enter to keep '" + selectedExpense.getDate() + "': ");
                String dateInput = scanner.nextLine().trim();
                if (dateInput.isEmpty()) {
                    break;
                }
                try {
                    selectedExpense.setDate(LocalDate.parse(dateInput));
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format.");
                }
            }

            String categoriesJsonResponse = HttpClient.makeGetRequest("http://localhost:8080/api/categories" + "/read/" + userId);
            if (categoriesJsonResponse == null || categoriesJsonResponse.isEmpty()) {
                System.out.println("No categories found.");
                return;
            }

            List<ExpenseCategory> categories = expenseCategoryService.parseCategoriesResponse(categoriesJsonResponse);
            if (categories.isEmpty()) {
                System.out.println("No categories found.");
                return;
            }
            System.out.println("Select a new category:");
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i).getName());
            }
            while (true) {
                System.out.print("Please select an option (or press Enter to keep '" + selectedExpense.getExpenseCategory().getName() + "'): ");
                String categoryInput = scanner.nextLine().trim();
                if (categoryInput.isEmpty()) {
                    break;
                }
                try {
                    int categoryIndex = Integer.parseInt(categoryInput) - 1;
                    if (categoryIndex >= 0 && categoryIndex < categories.size()) {
                        selectedExpense.setExpenseCategory(categories.get(categoryIndex));
                        break;
                    } else {
                        System.out.println("Invalid category selection.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
            }

            String updateExpenseResponse = HttpClient.makePutRequest("http://localhost:8080/api/expenses/update",
                    selectedExpense.toJsonString());
            if (updateExpenseResponse != null) {
                System.out.println("Expense updated successfully!");
            } else {
                System.out.println("Expense update failed. Please try again.");
            }
        }
    }

    @Override
    public void start(Long userId, String userFirstName, Scanner scanner, UiManager uiManager) {

    }
}
