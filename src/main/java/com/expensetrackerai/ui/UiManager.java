package com.expensetrackerai.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UiManager {

    private final MainUi mainUi;
    private final LoginUi loginUi;
    private final RegisterUi registerUi;
    private final DashboardUi dashboardUi;
    private final AddExpenseUi addExpenseUi;
    private final ViewExpensesUi viewExpensesUi;
    private final UpdateExpenseUi updateExpensesUi;
    private final ManageExpenseCategoriesUi manageExpenseCategoriesUi;
    private final DeleteExpenseUi deleteExpenseUi;

    @Autowired
    public UiManager(MainUi mainUi, LoginUi loginUi, RegisterUi registerUi, DashboardUi dashboardUi, AddExpenseUi addExpenseUi, ViewExpensesUi viewExpensesUi, UpdateExpenseUi updateExpensesUi, ManageExpenseCategoriesUi manageExpenseCategoriesUi, DeleteExpenseUi deleteExpenseUi) {
        this.mainUi = mainUi;
        this.loginUi = loginUi;
        this.registerUi = registerUi;
        this.dashboardUi = dashboardUi;
        this.addExpenseUi = addExpenseUi;
        this.viewExpensesUi = viewExpensesUi;
        this.updateExpensesUi = updateExpensesUi;
        this.deleteExpenseUi = deleteExpenseUi;
        this.manageExpenseCategoriesUi = manageExpenseCategoriesUi;
    }

    public void startMainUi(Scanner scanner) {
        mainUi.start(scanner, this);
    }

    public void startLoginUi(Scanner scanner) {
        loginUi.start(scanner, this);
    }

    public void startRegisterUi(Scanner scanner) {
        registerUi.start(scanner, this);
    }

    public void startDashboardUi(Long userId, String userFirstName, Scanner scanner) {
        dashboardUi.start(userId, userFirstName, scanner, this);
    }

    public void startAddExpenseUi(Long userId, Scanner scanner) {
        addExpenseUi.start(userId, scanner);
    }

    public void startViewExpensesUi(Long userId, Scanner scanner) {
        viewExpensesUi.start(userId, scanner);
    }

    public void startUpdateExpenseUi(Long userId, Scanner scanner) {
        updateExpensesUi.start(userId, scanner);
    }

    public void startDeleteExpenseUi(Long userId, Scanner scanner) {
        deleteExpenseUi.start(userId, scanner);
    }

    public void startManageExpenseCategoriesUi(Long userId, Scanner scanner) {
        manageExpenseCategoriesUi.start(userId, scanner);
    }
}