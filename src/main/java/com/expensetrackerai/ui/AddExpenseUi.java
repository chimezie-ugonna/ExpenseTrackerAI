package com.expensetrackerai.ui;

import com.expensetrackerai.model.ExpenseCategory;
import com.expensetrackerai.model.User;
import com.expensetrackerai.service.ExpenseCategoryService;
import com.expensetrackerai.util.HttpClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Component
public class AddExpenseUi implements UiComponent {

    private static final String BASE_URL = "http://localhost:8080/api/categories";
    private final ExpenseCategoryService expenseCategoryService;

    @Autowired
    public AddExpenseUi(ExpenseCategoryService expenseCategoryService) {
        this.expenseCategoryService = expenseCategoryService;
    }

    @Override
    public void start(Long userId, Scanner scanner) {
        System.out.println("\n--- Add Expense ---");

        String categoriesJsonResponse = HttpClient.makeGetRequest(BASE_URL + "/read/" + userId);
        if (categoriesJsonResponse == null || categoriesJsonResponse.isEmpty()) {
            System.out.println("No categories found. Please create one first.");
            return;
        }

        List<ExpenseCategory> categories = expenseCategoryService.parseCategoriesResponse(categoriesJsonResponse);
        if (categories.isEmpty()) {
            System.out.println("No categories found. Please create one first.");
            return;
        }

        System.out.println("Select a category:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getName());
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

                String createCategoryResponse = HttpClient.makePostRequest(BASE_URL + "/create", "user_id=" + userId + "&name=" + newCategoryName);
                if (createCategoryResponse != null) {
                    System.out.println("Category created successfully!");
                    JSONObject categoryJson = new JSONObject(createCategoryResponse);
                    long categoryId = categoryJson.getLong("id");
                    String categoryName = categoryJson.optString("name");
                    selectedCategory = new ExpenseCategory(categoryId, categoryName, new User());
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

        String createExpenseResponse = HttpClient.makePostRequest("http://localhost:8080/api/expenses" + "/create", "user_id=" + userId + "&amount=" + amount + "&description=" + description + "&category_id=" + selectedCategory.getId() + "&date=" + expenseDate);
        if (createExpenseResponse != null) {
            System.out.println("Expense added successfully!");
        } else {
            System.out.println("Failed to add expense. Please try again.");
        }
    }

    @Override
    public void start(Long userId, String userFirstName, Scanner scanner, UiManager uiManager) {

    }

    @Override
    public void start(Scanner scanner, UiManager uiManager) {

    }
}