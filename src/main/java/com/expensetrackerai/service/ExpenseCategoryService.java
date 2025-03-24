package com.expensetrackerai.service;

import com.expensetrackerai.model.ExpenseCategory;
import com.expensetrackerai.model.User;
import com.expensetrackerai.repository.ExpenseCategoryRepository;
import com.expensetrackerai.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final UserRepository userRepository;

    public ExpenseCategoryService(ExpenseCategoryRepository expenseCategoryRepository, UserRepository userRepository) {
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.userRepository = userRepository;
    }

    // Get both default categories and categories specific to the user
    public List<ExpenseCategory> getCategoriesByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch default categories (where user is null) and user-specific categories
        List<ExpenseCategory> categories = new ArrayList<>();
        categories.addAll(expenseCategoryRepository.findByUser(null));  // Default categories
        categories.addAll(expenseCategoryRepository.findByUser(user));  // User-specific categories

        return categories;
    }

    public ExpenseCategory createExpenseCategory(Long user_id, String category_name) {
        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setUserId(user_id);
        expenseCategory.setCategory_name(category_name);
        return expenseCategoryRepository.save(expenseCategory);
    }
}