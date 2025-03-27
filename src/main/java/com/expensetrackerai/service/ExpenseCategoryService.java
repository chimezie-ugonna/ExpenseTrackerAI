package com.expensetrackerai.service;

import com.expensetrackerai.model.ExpenseCategory;
import com.expensetrackerai.model.User;
import com.expensetrackerai.repository.ExpenseCategoryRepository;
import com.expensetrackerai.repository.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
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

    public ExpenseCategory createExpenseCategory(Long userId, String categoryName) {
        try {
            ExpenseCategory expenseCategory = new ExpenseCategory();
            expenseCategory.setUserId(userId);
            expenseCategory.setName(categoryName);
            return expenseCategoryRepository.save(expenseCategory);
        } catch (Exception e) {
            return null;
        }
    }

    public List<ExpenseCategory> getCategoriesByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<ExpenseCategory> categories = new ArrayList<>();
        categories.addAll(expenseCategoryRepository.findByUser(null));
        categories.addAll(expenseCategoryRepository.findByUser(user));

        return categories;
    }

    public List<ExpenseCategory> getCustomCategoriesByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Collections.emptyList();
        }
        return expenseCategoryRepository.findByUser(user);
    }

    public List<ExpenseCategory> parseCategoriesResponse(String jsonResponse) {
        try {
            JSONArray categoriesArray = new JSONArray(jsonResponse);
            List<ExpenseCategory> categories = new ArrayList<>();

            for (int i = 0; i < categoriesArray.length(); i++) {
                JSONObject obj = categoriesArray.getJSONObject(i);

                Long id = obj.getLong("id");
                String name = obj.optString("name");

                JSONObject userObj = obj.optJSONObject("user");
                User user = null;
                if (userObj != null) {
                    user = new User();
                    user.setId(userObj.getLong("id"));
                    user.setFullName(userObj.getString("fullName"));
                    user.setEmail(userObj.getString("email"));
                }

                categories.add(new ExpenseCategory(id, name, user));
            }

            return categories;
        } catch (Exception e) {
            System.out.println("Error parsing expenses response: " + e.getMessage());
            return List.of();
        }
    }

    public boolean deleteExpenseCategory(Long categoryId) {
        try {
            expenseCategoryRepository.deleteById(categoryId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error: " + e.getMessage());
        }
    }
}
