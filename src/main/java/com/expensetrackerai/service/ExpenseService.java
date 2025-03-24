package com.expensetrackerai.service;

import com.expensetrackerai.model.Expense;
import com.expensetrackerai.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
}