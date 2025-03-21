package com.expensetrackerai.controller;

import com.expensetrackerai.model.Expense;
import com.expensetrackerai.service.ExpenseService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/create")
    public Expense createExpense(@RequestParam Double amount, @RequestParam String description) {
        return expenseService.createExpense(amount, description);
    }
}