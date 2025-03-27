# Expense Tracker AI

## Overview

Expense Tracker AI is a simple terminal-based application designed to help users track their expenses. The application allows users to:
- Register and log in.
- Add expenses under predefined or custom categories.
- View and manage categories.
- Get a summary of their spending, including trends, predictions, and suggestions powered by Google gemini AI model.

## Features

- **Expense Tracking**: Users can add, view, and manage expenses.
- **Category Management**: Users can view predefined categories and add custom ones.
- **AI Integration**: Summarizes spending trends and makes suggestions using Google gemini AI model.
- **User Authentication**: Secure registration and login for each user.
- **Terminal UI**: The app uses a terminal interface for user interaction.

## Requirements

- JDK 11 or higher
- Maven
- MySQL 8+
- Google API Key for AI Integration

## Setup Instructions

 1. **Clone the Repository**

    ```bash
    git clone https://github.com/chimezie-ugonna/ExpenseTrackerAI.git
    ```

 2. **Navigate to the project folder**

   ```bash
   cd ExpenseTrackerAI
   ```
      

 3. **Database Setup**
    1.	Create a MySQL database named `expense_tracker_ai`.
  
        ```sql
        CREATE DATABASE expense_tracker_ai;
        ```


	  2.	Import the provided `database_setup.sql` file (found in the project root directory) to set up the necessary tables in the                  database.
 
       ```sql
       ExpenseTrackerAI/database_setup.sql;
       ```
       
    3.	In the application.yml file located in `src/main/resources/`, update the database connection settings with your MySQL credentials:


       ```yaml
        server:
          port: 8080
        
        spring:
          datasource:
            url: jdbc:mysql://localhost:3306/expense_tracker_ai
            username: <your_username>
            password: <your_password>
            driver-class-name: com.mysql.cj.jdbc.Driver
        
          jpa:
            hibernate:
              ddl-auto: update
            properties:
              hibernate:
                dialect: org.hibernate.dialect.MySQL8Dialect
                format_sql: true
                show-sql: true
        
          main:
            web-application-type: servlet
        ```


 4. **Set Up Google API Key**

    1. Visit the [Google API Key Generator](https://aistudio.google.com/app/u/1/apikey?_gl=1*qh6gbs*_ga*MTc3MTg1MzU1My4xNzM0NDE5MTcy*_ga_P1DBVKWT6V*MTczNDUxODEwMy4zLjEuMTczNDUxODExNC40OS4wLjE4NTkzMDc3MDA.&pli=1).
    2. Create an API key by following the steps on the site.
    3. Copy the created API key.
    4. In the `ExpenseController.java class`, replace the following line with your API key:
    
    ```java
    private static final String API_KEY = "Your API Key";
    ```



  5. **Running the Application**

      You can run the application in two ways:
      
     **Option 1**: Via Terminal
      	1.	Navigate to your project directory in the terminal.
      	2.	Run the application using Maven:
      
            ```bash
            mvn spring-boot:run
      	     ```
      	     
        This will start the application and launch the terminal UI.

      **Option 2**: Via IDE (IntelliJ IDEA, Eclipse, etc.)
        1.	Open the project in your IDE.
        2.	Locate the `ExpenseTrackerAiApplication.java` class.
        3.	Click the play button (or right-click and select “Run”).

      This will start the Spring Boot application and launch the terminal UI.

  6. **Terminal UI Usage**

      Once the application is running, you’ll be presented with a terminal interface where you can:
     - Register a new account or Login with your existing credentials.
     - Add expenses under predefined categories or create your own custom categories.
     - View and manage categories.
     - View your expense summary, including insights and suggestions powered by Google AI.

  7. **Testing the Application**

      You can test the application directly via the terminal, interacting with the UI to perform tasks like:
      - Registering and logging in.
      - Adding and managing expenses.
      - Viewing the expense summary with AI-generated insights.

### Contributing

If you’d like to contribute to this project:
1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Submit a pull request with clear descriptions and test cases.

### License

This project is for academic purposes only. No formal license is provided, and usage is restricted to the course requirements and evaluation by the instructor.
