-- Creating the tables again as per your schema requirements
CREATE DATABASE IF NOT EXISTS expense_tracker_ai;
USE expense_tracker_ai;

-- Create the users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

SELECT * FROM users;

-- Create the expenses table
CREATE TABLE IF NOT EXISTS expenses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    amount DECIMAL(10,2),
    description VARCHAR(255),
    date DATE,
    category_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

SELECT * FROM expenses;

-- Create the categories table
CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

SELECT * FROM categories;

-- Inserting predefined categories
INSERT INTO categories (name) VALUES
('Food'),
('Transportation'),
('Entertainment'),
('Healthcare'),
('Utilities'),
('Housing'),
('Insurance'),
('Education'),
('Savings'),
('Personal Care'),
('Clothing'),
('Groceries'),
('Dining Out'),
('Gifts'),
('Subscriptions'),
('Travel'),
('Fitness'),
('Home Maintenance'),
('Childcare'),
('Pet Care'),
('Miscellaneous');

-- Trigger for checking expenses before deleting a category
DELIMITER //

CREATE TRIGGER before_category_delete
BEFORE DELETE ON categories
FOR EACH ROW
BEGIN
    DECLARE expense_count INT;

    -- Check if there are any expenses related to the category
    SELECT COUNT(*) INTO expense_count
    FROM expenses
    WHERE category_id = OLD.id;

    -- If there are expenses related to the category, prevent deletion
    IF expense_count > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot delete category because it has associated expenses.';
    END IF;
END //

DELIMITER ;

SHOW TRIGGERS;