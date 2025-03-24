package com.expensetrackerai.repository;

import com.expensetrackerai.model.Expense;
import com.expensetrackerai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);
}