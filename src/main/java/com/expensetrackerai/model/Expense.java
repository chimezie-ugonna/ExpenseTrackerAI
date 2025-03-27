package com.expensetrackerai.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private String description;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ExpenseCategory expenseCategory;

    public Expense() {
    }

    public Expense(Long id, double amount, String description, LocalDate date, User user, ExpenseCategory expenseCategory) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.user = user;
        this.expenseCategory = expenseCategory;
    }

    public Long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ExpenseCategory getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(ExpenseCategory expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public void setUserId(Long userId) {
        this.user = new User();
        this.user.setId(userId);
    }

    public void setCategoryId(Long expenseCategoryId) {
        this.expenseCategory = new ExpenseCategory();
        this.expenseCategory.setId(expenseCategoryId);
    }

    public String toJsonString() {
        return "{"
                + "\"id\":" + this.getId() + ","
                + "\"categoryId\":" + this.getExpenseCategory().getId() + ","
                + "\"amount\":" + this.getAmount() + ","
                + "\"description\":\"" + this.getDescription() + "\","
                + "\"date\":\"" + this.getDate() + "\""
                + "}";
    }
}