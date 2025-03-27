package com.expensetrackerai.ui;

import com.expensetrackerai.model.ExpenseCategory;
import com.expensetrackerai.service.ExpenseCategoryService;
import com.expensetrackerai.util.HttpClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ManageExpenseCategoriesUi implements UiComponent {
    private static final String BASE_URL = "http://localhost:8080/api/categories";
    private final ExpenseCategoryService expenseCategoryService;

    public ManageExpenseCategoriesUi(ExpenseCategoryService expenseCategoryService) {
        this.expenseCategoryService = expenseCategoryService;
    }

    @Override
    public void start(Scanner scanner, UiManager uiManager) {

    }

    @Override
    public void start(Long userId, Scanner scanner) {
        while (true) {
            System.out.println("\n--- Manage Expense Categories ---");
            System.out.println("1. Add a Custom Expense Category");
            System.out.println("2. View Custom Expense Categories");
            System.out.println("3. Delete a Custom Expense Category");
            System.out.println("4. Go Back");
            System.out.print("Please select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    addExpenseCategory(scanner, userId);
                    break;
                case "2":
                    viewCustomCategories(userId);
                    break;
                case "3":
                    deleteExpenseCategory(scanner, userId);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    @Override
    public void start(Long userId, String userFirstName, Scanner scanner, UiManager uiManager) {

    }

    private void addExpenseCategory(Scanner scanner, Long userId) {
        while (true) {
            System.out.print("Enter new category name: ");
            String newCategoryName = scanner.nextLine().trim();
            if (newCategoryName.isEmpty()) {
                System.out.println("Category name cannot be empty.");
                continue;
            }
            String addCategoryResponse = HttpClient.makePostRequest(BASE_URL + "/create", "user_id=" + userId + "&name=" + newCategoryName);
            if (addCategoryResponse != null) {
                System.out.println("Category added successfully!");
                break;
            } else {
                System.out.println("Failed to add category. Try again.");
            }
        }
    }

    private void viewCustomCategories(Long userId) {
        String categoriesJsonResponse = HttpClient.makeGetRequest(BASE_URL + "/read/custom/" + userId);
        if (categoriesJsonResponse == null || categoriesJsonResponse.isEmpty()) {
            System.out.println("No custom categories found.");
            return;
        }

        List<ExpenseCategory> categories = expenseCategoryService.parseCategoriesResponse(categoriesJsonResponse);
        if (categories.isEmpty()) {
            System.out.println("No custom categories found.");
            return;
        }

        System.out.println("\nYour Custom Expense Categories:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getName());
        }
    }

    private void deleteExpenseCategory(Scanner scanner, Long userId) {
        while (true) {
            String categoriesJsonResponse = HttpClient.makeGetRequest(BASE_URL + "/read/custom/" + userId);
            if (categoriesJsonResponse == null || categoriesJsonResponse.isEmpty()) {
                System.out.println("No custom categories to delete.");
                return;
            }

            List<ExpenseCategory> categories = expenseCategoryService.parseCategoriesResponse(categoriesJsonResponse);
            if (categories.isEmpty()) {
                System.out.println("No custom categories to delete.");
                return;
            }

            System.out.println("\nSelect a category to delete:");
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i).getName());
            }

            int choice;
            while (true) {
                System.out.print("Please select an option: ");
                try {
                    choice = Integer.parseInt(scanner.nextLine().trim());
                    if (choice < 1 || choice > categories.size()) {
                        System.out.println("Invalid choice. Try again.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Enter a number.");
                }
            }

            ExpenseCategory categoryToDelete = categories.get(choice - 1);
            String deleteResponse = HttpClient.makeDeleteRequest(BASE_URL + "/delete/" + categoryToDelete.getId());
            if (deleteResponse != null) {
                if (deleteResponse.contains("Category deleted successfully")) {
                    System.out.println("Category deleted successfully.");
                    return;
                } else if (deleteResponse.contains("Cannot delete category")) {
                    System.out.println("Category cannot be deleted because it has associated expenses. Delete those expenses first.");
                } else {
                    System.out.println("Category deletion failed: " + deleteResponse);
                }
            } else {
                System.out.println("Failed to delete category. Server did not respond.");
            }

            while (true) {
                System.out.print("Would you like to try again? (yes/no): ");
                String retry = scanner.nextLine().trim().toLowerCase();
                if (retry.equals("yes")) {
                    break;
                } else if (retry.equals("no")) {
                    return;
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }
        }
    }
}
