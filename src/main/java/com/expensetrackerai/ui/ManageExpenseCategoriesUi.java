package com.expensetrackerai.ui;

import com.expensetrackerai.model.ExpenseCategory;
import com.expensetrackerai.service.ExpenseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ManageExpenseCategoriesUi {
    private final ExpenseCategoryService categoryService;

    @Autowired
    public ManageExpenseCategoriesUi(ExpenseCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void start(Scanner scanner, Long userId) {
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

    private void addExpenseCategory(Scanner scanner, Long userId) {
        while (true) {
            System.out.print("Enter new category name: ");
            String newCategoryName = scanner.nextLine().trim();
            if (newCategoryName.isEmpty()) {
                System.out.println("Category name cannot be empty.");
                continue;
            }

            try {
                categoryService.createExpenseCategory(userId, newCategoryName);
                System.out.println("Category added successfully!");
                break;
            } catch (Exception e) {
                System.out.println("Failed to add category: " + e.getMessage());
            }
        }
    }

    private void viewCustomCategories(Long userId) {
        List<ExpenseCategory> categories = categoryService.getCustomCategoriesByUserId(userId);
        if (categories.isEmpty()) {
            System.out.println("No custom categories found.");
            return;
        }
        System.out.println("\nYour Custom Expense Categories:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getCategory_name());
        }
    }

    private void deleteExpenseCategory(Scanner scanner, Long userId) {
        mainLoop:
        while (true) {
            List<ExpenseCategory> categories = categoryService.getCustomCategoriesByUserId(userId);
            if (categories.isEmpty()) {
                System.out.println("No custom categories available to delete.");
                return;
            }

            System.out.println("\nSelect a category to delete:");
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i).getCategory_name());
            }
            System.out.print("Please select an option: ");

            int choice;
            while (true) {
                try {
                    choice = Integer.parseInt(scanner.nextLine().trim());
                    if (choice < 1 || choice > categories.size()) {
                        System.out.println("Invalid choice. Try again.");
                        continue mainLoop;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Enter a number.");
                }
            }

            ExpenseCategory categoryToDelete = categories.get(choice - 1);
            try {
                if (categoryService.deleteExpenseCategory(categoryToDelete.getCategory_id())) {
                    System.out.println("Category deleted successfully!");
                    return;
                } else {
                    System.out.println("Failed to delete category: " + categoryToDelete.getCategory_name());
                }
            } catch (Exception e) {
                System.out.println("Failed to delete category: " + e.getMessage());
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
