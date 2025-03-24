package com.expensetrackerai.controller;

import com.expensetrackerai.model.Expense;
import com.expensetrackerai.service.ExpenseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/create")
    public Expense createExpense(
            @RequestParam Long user_id,
            @RequestParam Double amount,
            @RequestParam String description,
            @RequestParam Long category_id,
            @RequestParam String date
    ) {
        return expenseService.createExpense(user_id, amount, description, category_id, date);
    }
}