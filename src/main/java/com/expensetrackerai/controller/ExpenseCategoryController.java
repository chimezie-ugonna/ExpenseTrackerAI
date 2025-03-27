package com.expensetrackerai.controller;

import com.expensetrackerai.model.ExpenseCategory;
import com.expensetrackerai.service.ExpenseCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class ExpenseCategoryController {

    private final ExpenseCategoryService expenseCategoryService;

    public ExpenseCategoryController(ExpenseCategoryService expenseCategoryService) {
        this.expenseCategoryService = expenseCategoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createExpenseCategory(@RequestParam Long user_id, @RequestParam String name) {
        try {
            ExpenseCategory expenseCategory = expenseCategoryService.createExpenseCategory(user_id, name);
            return ResponseEntity.ok(expenseCategory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/read/{userId}")
    public ResponseEntity<?> getExpenseCategories(@PathVariable Long userId) {
        try {
            List<ExpenseCategory> expenseCategories = expenseCategoryService.getCategoriesByUserId(userId);
            return ResponseEntity.ok(expenseCategories);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching expense categories: " + e.getMessage());
        }
    }

    @GetMapping("/read/custom/{userId}")
    public ResponseEntity<?> getCustomExpenseCategories(@PathVariable Long userId) {
        try {
            List<ExpenseCategory> expenseCategories = expenseCategoryService.getCustomCategoriesByUserId(userId);
            return ResponseEntity.ok(expenseCategories);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching expense categories: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteExpenseCategory(@PathVariable Long id) {
        try {
            boolean deleted = expenseCategoryService.deleteExpenseCategory(id);
            if (deleted) {
                return ResponseEntity.ok("Category deleted successfully.");
            } else {
                return ResponseEntity.status(500).body("Failed to delete category.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unexpected error: " + e.getMessage());
        }
    }
}