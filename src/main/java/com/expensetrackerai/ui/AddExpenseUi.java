package com.expensetrackerai.ui;

import com.expensetrackerai.model.ExpenseCategory;
import com.expensetrackerai.service.ExpenseCategoryService;
import com.expensetrackerai.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Component
public class AddExpenseUi {
    private final ExpenseCategoryService categoryService;
    private final ExpenseService expenseService;

    @Autowired
    public AddExpenseUi(ExpenseCategoryService categoryService, ExpenseService expenseService) {
        this.categoryService = categoryService;
        this.expenseService = expenseService;
    }

    public void start(Scanner scanner, Long userId) {
        System.out.println("\n--- Add Expense ---");

        List<ExpenseCategory> categories = categoryService.getCategoriesByUserId(userId);
        if (categories.isEmpty()) {
            System.out.println("No categories found. Please create one first.");
            return;
        }

        System.out.println("Select a category:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getCategory_name());
        }
        System.out.println((categories.size() + 1) + ". Create a new category");
        System.out.println((categories.size() + 2) + ". Go Back");

        int categoryChoice;
        while (true) {
            System.out.print("Please select an option: ");
            try {
                categoryChoice = Integer.parseInt(scanner.nextLine().trim());
                if (categoryChoice < 1 || categoryChoice > categories.size() + 2) {
                    System.out.println("Invalid choice. Try again.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number.");
            }
        }

        ExpenseCategory selectedCategory;

        if (categoryChoice == categories.size() + 1) {
            while (true) {
                System.out.print("Enter new category name: ");
                String newCategoryName = scanner.nextLine().trim();

                if (newCategoryName.isEmpty()) {
                    System.out.println("Category name cannot be empty. Please enter a valid name.");
                    continue;
                }

                selectedCategory = categoryService.createExpenseCategory(userId, newCategoryName);
                if (selectedCategory != null) {
                    System.out.println("Category created successfully!");
                    break;
                } else {
                    System.out.println("Failed to create category. Try again.");
                }
            }
        } else if (categoryChoice == categories.size() + 2) {
            return;
        } else {
            selectedCategory = categories.get(categoryChoice - 1);
        }

        System.out.print("Enter expense amount in Euro: ");
        double amount;
        while (true) {
            try {
                amount = Double.parseDouble(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.print("Invalid amount. Enter a valid number: ");
            }
        }

        System.out.print("Enter expense description: ");
        String description = scanner.nextLine().trim();

        LocalDate expenseDate = null;
        while (expenseDate == null) {
            System.out.print("Enter date (YYYY-MM-DD) or press Enter to use today’s date: ");
            String dateInput = scanner.nextLine().trim();

            if (dateInput.isEmpty()) {
                expenseDate = LocalDate.now();
            } else {
                try {
                    expenseDate = LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
                }
            }
        }

        if (expenseService.createExpense(userId, amount, description, selectedCategory.getCategory_id(), expenseDate.toString()) != null) {
            System.out.println("Expense added successfully!");
        } else {
            System.out.println("Failed to add expense. Please try again.");
        }
    }
}