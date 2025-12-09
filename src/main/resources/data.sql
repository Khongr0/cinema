-- Инициализация тестовых пользователей для MySQL
-- Пароль для всех: test123
-- Хеш пароля (BCrypt): $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O

-- Администратор
INSERT IGNORE INTO users (username, password, email, full_name, role, created_at) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O', 
        'admin@cinema.ru', 'Администратор Системы', 'ADMIN', NOW());

-- Кассир
INSERT IGNORE INTO users (username, password, email, full_name, role, created_at) 
VALUES ('cashier', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O', 
        'cashier@cinema.ru', 'Кассир Иванов Иван', 'CASHIER', NOW());

-- Зритель
INSERT IGNORE INTO users (username, password, email, full_name, role, created_at) 
VALUES ('viewer', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O', 
        'viewer@cinema.ru', 'Зритель Петров Петр', 'VIEWER', NOW());
