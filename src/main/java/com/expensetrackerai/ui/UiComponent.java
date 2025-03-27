package com.expensetrackerai.ui;

import java.util.Scanner;

public interface UiComponent {
    void start(Scanner scanner, UiManager uiManager);

    void start(Long userId, Scanner scanner);

    void start(Long userId, String userFirstName, Scanner scanner, UiManager uiManager);
}
