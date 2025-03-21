package com.expensetrackerai.service;

import com.expensetrackerai.model.Expense;
import com.expensetrackerai.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense createExpense(Double amount, String description) {
        Expense expense = new Expense();
        expense.setAmount(amount);
        expense.setDescription(description);
        return expenseRepository.save(expense);
    }
}