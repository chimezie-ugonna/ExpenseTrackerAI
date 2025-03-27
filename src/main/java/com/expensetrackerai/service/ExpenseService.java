package com.expensetrackerai.service;

import com.expensetrackerai.model.Expense;
import com.expensetrackerai.model.ExpenseCategory;
import com.expensetrackerai.model.User;
import com.expensetrackerai.repository.ExpenseCategoryRepository;
import com.expensetrackerai.repository.ExpenseRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository, ExpenseCategoryRepository expenseCategoryRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseCategoryRepository = expenseCategoryRepository;
    }

    public Expense createExpense(Long userId, Double amount, String description, Long categoryId, String date) {
        Expense expense = new Expense();
        expense.setUserId(userId);
        expense.setAmount(amount);
        expense.setDescription(description);
        expense.setCategoryId(categoryId);
        expense.setDate(LocalDate.parse(date));

        return expenseRepository.save(expense);
    }

    public List<Expense> getExpensesByUserId(Long userId) {
        User user = new User();
        user.setId(userId);
        return expenseRepository.findByUser(user);
    }

    public List<Expense> parseExpensesResponse(String jsonResponse) {
        try {
            JSONArray expensesArray = new JSONArray(jsonResponse);
            List<Expense> expenses = new ArrayList<>();

            for (int i = 0; i < expensesArray.length(); i++) {
                JSONObject obj = expensesArray.getJSONObject(i);

                Long id = obj.getLong("id");
                double amount = obj.getDouble("amount");
                String description = obj.optString("description", "");
                LocalDate date = LocalDate.parse(obj.getString("date"));

                JSONObject userObj = obj.getJSONObject("user");
                User user = new User();
                user.setId(userObj.getLong("id"));
                user.setFullName(userObj.getString("fullName"));
                user.setEmail(userObj.getString("email"));

                JSONObject categoryObj = obj.getJSONObject("expenseCategory");
                ExpenseCategory category = new ExpenseCategory();
                category.setId(categoryObj.getLong("id"));
                category.setName(categoryObj.getString("name"));

                expenses.add(new Expense(id, amount, description, date, user, category));
            }

            return expenses;
        } catch (Exception e) {
            System.out.println("Error parsing expenses response: " + e.getMessage());
            return List.of();
        }
    }

    public Expense updateExpense(Expense updatedExpense) {
        if (updatedExpense != null) {
            Optional<Expense> existingExpenseOpt = expenseRepository.findById(updatedExpense.getId());

            if (existingExpenseOpt.isPresent()) {
                Expense existingExpense = existingExpenseOpt.get();

                existingExpense.setAmount(updatedExpense.getAmount());
                existingExpense.setDescription(updatedExpense.getDescription());
                existingExpense.setDate(updatedExpense.getDate());

                Optional<ExpenseCategory> categoryOpt = expenseCategoryRepository.findById(updatedExpense.getExpenseCategory().getId());
                if (categoryOpt.isPresent()) {
                    existingExpense.setExpenseCategory(categoryOpt.get());
                } else {
                    System.out.println("Invalid categoryId: " + updatedExpense.getExpenseCategory().getId());
                    return null;
                }

                return expenseRepository.save(existingExpense);
            } else {
                System.out.println("Expense not found with ID: " + updatedExpense.getId());
                return null;
            }
        } else {
            return null;
        }
    }

    public boolean deleteExpense(Long expenseId) {
        try {
            expenseRepository.deleteById(expenseId);
            return true;
        } catch (Exception e) {
            System.out.println("Error while deleting expense: " + e.getMessage());
            return false;
        }
    }
}