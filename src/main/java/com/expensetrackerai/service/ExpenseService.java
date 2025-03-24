package com.expensetrackerai.service;

import com.expensetrackerai.model.Expense;
import com.expensetrackerai.model.User;
import com.expensetrackerai.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
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