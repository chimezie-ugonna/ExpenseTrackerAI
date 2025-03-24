package com.expensetrackerai.repository;

import com.expensetrackerai.model.ExpenseCategory;
import com.expensetrackerai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {
    List<ExpenseCategory> findByUser(User user);
}