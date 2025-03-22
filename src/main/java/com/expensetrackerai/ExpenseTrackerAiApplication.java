package com.expensetrackerai;
import com.expensetrackerai.ui.MainUi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExpenseTrackerAiApplication {
    public static void main(String[] args) throws InterruptedException {
        Thread springBootThread = new Thread(() -> {
            SpringApplication.run(ExpenseTrackerAiApplication.class, args);
        });

        springBootThread.start();

        springBootThread.join();

        MainUi.main(args);
    }
}
