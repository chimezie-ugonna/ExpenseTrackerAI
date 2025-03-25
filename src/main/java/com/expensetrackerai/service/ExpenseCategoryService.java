package com.expensetrackerai.service;

import com.expensetrackerai.model.ExpenseCategory;
import com.expensetrackerai.model.User;
import com.expensetrackerai.repository.ExpenseCategoryRepository;
import com.expensetrackerai.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final UserRepository userRepository;

    public ExpenseCategoryService(ExpenseCategoryRepository expenseCategoryRepository, UserRepository userRepository) {
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.userRepository = userRepository;
    }

    public List<ExpenseCategory> getCategoriesByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<ExpenseCategory> categories = new ArrayList<>();
        categories.addAll(expenseCategoryRepository.findByUser(null));
        categories.addAll(expenseCategoryRepository.findByUser(user));

        return categories;
    }

    public ExpenseCategory createExpenseCategory(Long userId, String categoryName) {
        try {
            ExpenseCategory expenseCategory = new ExpenseCategory();
            expenseCategory.setUserId(userId);
            expenseCategory.setCategory_name(categoryName);
            return expenseCategoryRepository.save(expenseCategory);
        } catch (Exception e) {
            return null;
        }
    }

    public List<ExpenseCategory> getCustomCategoriesByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Collections.emptyList();
        }
        return expenseCategoryRepository.findByUser(user);
    }

    public boolean deleteExpenseCategory(Long categoryId) {
        try {
            expenseCategoryRepository.deleteById(categoryId);
            return true;
        } catch (Exception e) {
            System.out.println("Error while deleting expense category: " + e.getMessage());
            return false;
        }
    }
}
