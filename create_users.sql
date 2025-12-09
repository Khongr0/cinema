-- SQL скрипт для создания тестовых пользователей в MySQL
-- Использование: mysql -u root -p cinema_db < create_users.sql

USE cinema_db;

-- Администратор (пароль: test123)
INSERT IGNORE INTO users (username, password, email, full_name, role, created_at) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O', 
        'admin@cinema.ru', 'Администратор Системы', 'ADMIN', NOW());

-- Кассир (пароль: test123)
INSERT IGNORE INTO users (username, password, email, full_name, role, created_at) 
VALUES ('cashier', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O', 
        'cashier@cinema.ru', 'Кассир Иванов Иван', 'CASHIER', NOW());

-- Зритель (пароль: test123)
INSERT IGNORE INTO users (username, password, email, full_name, role, created_at) 
VALUES ('viewer', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O', 
        'viewer@cinema.ru', 'Зритель Петров Петр', 'VIEWER', NOW());

SELECT 'Пользователи созданы успешно!' AS message;

