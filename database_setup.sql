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

-- Insert historical expenses for User ID 1 (You can use whatever user id you want)
INSERT INTO expenses (user_id, amount, description, date, category_id) VALUES
-- October 2024
(1, 120.00, 'Grocery shopping at Walmart', '2024-10-05', (SELECT id FROM categories WHERE name = 'Groceries')),
(1, 25.00, 'Uber ride to office', '2024-10-07', (SELECT id FROM categories WHERE name = 'Transportation')),
(1, 15.99, 'Netflix Subscription', '2024-10-10', (SELECT id FROM categories WHERE name = 'Subscriptions')),
(1, 800.00, 'October Rent Payment', '2024-10-01', (SELECT id FROM categories WHERE name = 'Housing')),

-- November 2024
(1, 110.00, 'Grocery shopping at Target', '2024-11-03', (SELECT id FROM categories WHERE name = 'Groceries')),
(1, 30.00, 'Public transport monthly pass', '2024-11-08', (SELECT id FROM categories WHERE name = 'Transportation')),
(1, 15.99, 'Spotify Premium Subscription', '2024-11-11', (SELECT id FROM categories WHERE name = 'Subscriptions')),
(1, 800.00, 'November Rent Payment', '2024-11-01', (SELECT id FROM categories WHERE name = 'Housing')),
(1, 45.00, 'Dinner at a fancy restaurant', '2024-11-20', (SELECT id FROM categories WHERE name = 'Dining Out')),

-- December 2024
(1, 150.00, 'Christmas Gift Shopping', '2024-12-15', (SELECT id FROM categories WHERE name = 'Gifts')),
(1, 95.00, 'New Year Party Celebration', '2024-12-31', (SELECT id FROM categories WHERE name = 'Entertainment')),
(1, 800.00, 'December Rent Payment', '2024-12-01', (SELECT id FROM categories WHERE name = 'Housing')),
(1, 20.00, 'Taxi Fare', '2024-12-05', (SELECT id FROM categories WHERE name = 'Transportation')),

-- January 2025
(1, 130.00, 'Grocery shopping at Walmart', '2025-01-05', (SELECT id FROM categories WHERE name = 'Groceries')),
(1, 50.00, 'Electricity Bill Payment', '2025-01-10', (SELECT id FROM categories WHERE name = 'Utilities')),
(1, 30.00, 'Gym Membership Fee', '2025-01-15', (SELECT id FROM categories WHERE name = 'Fitness')),
(1, 800.00, 'January Rent Payment', '2025-01-01', (SELECT id FROM categories WHERE name = 'Housing')),
(1, 10.99, 'Hulu Subscription', '2025-01-12', (SELECT id FROM categories WHERE name = 'Subscriptions')),

-- February 2025
(1, 120.00, 'Grocery shopping at Costco', '2025-02-06', (SELECT id FROM categories WHERE name = 'Groceries')),
(1, 40.00, 'Fuel for car', '2025-02-07', (SELECT id FROM categories WHERE name = 'Transportation')),
(1, 15.99, 'Disney+ Subscription', '2025-02-15', (SELECT id FROM categories WHERE name = 'Subscriptions')),
(1, 800.00, 'February Rent Payment', '2025-02-01', (SELECT id FROM categories WHERE name = 'Housing')),
(1, 85.00, 'Doctor Consultation Fee', '2025-02-20', (SELECT id FROM categories WHERE name = 'Healthcare')),

-- March 2025
(1, 150.00, 'Grocery shopping at Whole Foods', '2025-03-07', (SELECT id FROM categories WHERE name = 'Groceries')),
(1, 45.00, 'Taxi ride to airport', '2025-03-10', (SELECT id FROM categories WHERE name = 'Transportation')),
(1, 15.99, 'HBO Max Subscription', '2025-03-12', (SELECT id FROM categories WHERE name = 'Subscriptions')),
(1, 800.00, 'March Rent Payment', '2025-03-01', (SELECT id FROM categories WHERE name = 'Housing')),
(1, 65.00, 'Dinner at a steakhouse', '2025-03-14', (SELECT id FROM categories WHERE name = 'Dining Out')),
(1, 30.00, 'Water Bill Payment', '2025-03-18', (SELECT id FROM categories WHERE name = 'Utilities')),
(1, 75.00, 'Annual Health Checkup', '2025-03-22', (SELECT id FROM categories WHERE name = 'Healthcare')),
(1, 200.00, 'Weekend Getaway Trip', '2025-03-25', (SELECT id FROM categories WHERE name = 'Travel'));

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
