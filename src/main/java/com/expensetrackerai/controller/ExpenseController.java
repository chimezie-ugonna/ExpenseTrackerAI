package com.expensetrackerai.controller;

import com.expensetrackerai.model.Expense;
import com.expensetrackerai.service.ExpenseService;
import com.expensetrackerai.util.HttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private static final Logger logger = Logger.getLogger(ExpenseController.class.getName());
    private static final String API_KEY = "Your API Key";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    private static StringBuilder getStringBuilder(List<Expense> expenses) {
        StringBuilder expenseList = new StringBuilder();

        for (Expense expense : expenses) {
            expenseList.append(String.format("Amount: %.2f, Category: %s, Description: %s, Date: %s\n", expense.getAmount(), expense.getExpenseCategory().getName(), expense.getDescription(), expense.getDate().toString()));
        }
        return expenseList;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createExpense(@RequestParam Long user_id, @RequestParam Double amount, @RequestParam String description, @RequestParam Long category_id, @RequestParam String date) {
        try {
            Expense newExpense = expenseService.createExpense(user_id, amount, description, category_id, date);
            return ResponseEntity.ok(newExpense);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/read/{userId}")
    public ResponseEntity<?> getUserExpenses(@PathVariable Long userId) {
        try {
            List<Expense> expenses = expenseService.getExpensesByUserId(userId);
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching expenses: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateExpense(@RequestBody Expense updatedExpense) {
        try {
            Expense expense = expenseService.updateExpense(updatedExpense);
            if (expense != null) {
                return ResponseEntity.ok("Expense updated successfully.");
            } else {
                return ResponseEntity.status(500).body("Expense update failed. Please try again.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{expenseId}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long expenseId) {
        try {
            boolean deleted = expenseService.deleteExpense(expenseId);
            if (deleted) {
                return ResponseEntity.ok("Expense deleted successfully.");
            } else {
                return ResponseEntity.status(500).body("Failed to delete expense.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/aiSummary/{userId}")
    public ResponseEntity<?> generateAISummary(@PathVariable Long userId) {
        try {
            List<Expense> expenses = expenseService.getExpensesByUserId(userId);
            String jsonData = generateJsonData(expenses);
            String aiSummary = HttpClient.makePostJsonRequest(API_URL, jsonData);
            if (aiSummary != null) {
                return ResponseEntity.ok(extractAISummary(aiSummary));
            } else {
                return ResponseEntity.status(500).body("Failed to retrieve AI summary.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    private String extractAISummary(String jsonResponse) {
        try {
            JSONObject responseJson = new JSONObject(jsonResponse);
            JSONArray candidates = responseJson.getJSONArray("candidates");

            if (!candidates.isEmpty()) {
                JSONObject firstCandidate = candidates.getJSONObject(0);
                JSONObject content = firstCandidate.getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");

                StringBuilder summary = new StringBuilder();
                for (int i = 0; i < parts.length(); i++) {
                    summary.append(parts.getJSONObject(i).getString("text")).append("\n\n");
                }

                return summary.toString().trim();
            }
        } catch (Exception e) {
            logger.severe("An error occurred: " + e.getMessage());
            return "Error processing AI summary.";
        }
        return "No AI summary available.";
    }

    private String generateJsonData(List<Expense> expenses) {
        StringBuilder expenseList = getStringBuilder(expenses);

        String prompt = "You are an AI assistant for an Expense Tracker application. Your task is to analyze the following expense data and generate a detailed, structured, user-friendly financial summary. " + "Do not use asterisks or markdown formatting—only provide clear, well-structured plain text. " + "Your summary should include:\n" + "- Total spending (overall and categorized breakdown)\n" + "- Top expense categories (with percentage of total spending)\n" + "- Unusual transactions (highlight outliers or anomalies, including any significant or unexpected transactions)\n" + "- Spending trends (include any notable patterns over time, like increasing or decreasing spending in specific categories)\n" + "- Comparative analysis (compare this period with the previous period, highlighting any changes in spending habits)\n" + "- Savings opportunities (suggest areas where spending can be reduced)\n" + "- Budgeting advice (suggest budgeting strategies or allocation of funds for the future)\n" + "- Recommendations for financial goals (e.g., reducing debt, increasing savings)\n" + "- Financial health score (optional, if possible, provide a health score or an assessment of the user's financial situation)\n" + "- Key insights (any other noteworthy trends, such as recurring expenses that could be reduced or avoided)\n\n" + "Expense Data in Euro:\n" + expenseList;

        JSONObject textPart = new JSONObject();
        textPart.put("text", prompt);

        JSONObject partsObject = new JSONObject();
        partsObject.put("parts", new JSONArray().put(textPart));

        JSONArray contentsArray = new JSONArray();
        contentsArray.put(partsObject);

        JSONObject finalJson = new JSONObject();
        finalJson.put("contents", contentsArray);

        return finalJson.toString();
    }
}