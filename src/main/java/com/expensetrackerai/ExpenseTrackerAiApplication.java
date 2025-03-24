package com.expensetrackerai;

import com.expensetrackerai.ui.UiManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

@SpringBootApplication
public class ExpenseTrackerAiApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ExpenseTrackerAiApplication.class, args);
        UiManager uiManager = context.getBean(UiManager.class);
        Scanner scanner = new Scanner(System.in);
        uiManager.startMainUi(scanner);
    }
}
