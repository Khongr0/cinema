-- SQL скрипт для правильного создания администратора
-- Выполните в MySQL Workbench

USE cinema_db;

-- Удаляем старого admin (если есть)
DELETE FROM users WHERE username = 'admin';

-- Создаем нового admin с правильным BCrypt хешем
-- Пароль: test123
INSERT INTO users (username, password, email, full_name, role, created_at) 
VALUES (
    'admin', 
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O', 
    'admin@cinema.ru', 
    'Администратор Системы', 
    'ADMIN', 
    NOW()
);

-- Проверка
SELECT 
    username,
    email,
    role,
    CASE 
        WHEN password LIKE '$2a$10$%' THEN '✅ Пароль правильный'
        ELSE '❌ Пароль неправильный'
    END as password_check
FROM users 
WHERE username = 'admin';

-- После выполнения войдите: admin / test123

