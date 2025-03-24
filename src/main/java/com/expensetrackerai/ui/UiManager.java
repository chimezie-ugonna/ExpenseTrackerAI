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

    @Autowired
    public UiManager(MainUi mainUi, LoginUi loginUi, RegisterUi registerUi, DashboardUi dashboardUi) {
        this.mainUi = mainUi;
        this.loginUi = loginUi;
        this.registerUi = registerUi;
        this.dashboardUi = dashboardUi;
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
}