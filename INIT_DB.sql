-- SQL скрипт для инициализации базы данных
-- Выполните этот скрипт после создания базы данных cinema_db

-- Создание тестовых пользователей
-- Пароль для всех: test123

-- Администратор
INSERT INTO users (username, password, email, full_name, role, created_at) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O', 
        'admin@cinema.ru', 'Администратор Системы', 'ADMIN', NOW())
ON DUPLICATE KEY UPDATE username=username;

-- Кассир
INSERT INTO users (username, password, email, full_name, role, created_at) 
VALUES ('cashier', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O', 
        'cashier@cinema.ru', 'Кассир Иванов Иван', 'CASHIER', NOW())
ON DUPLICATE KEY UPDATE username=username;

-- Зритель
INSERT INTO users (username, password, email, full_name, role, created_at) 
VALUES ('viewer', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O', 
        'viewer@cinema.ru', 'Зритель Петров Петр', 'VIEWER', NOW())
ON DUPLICATE KEY UPDATE username=username;

