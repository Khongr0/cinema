-- SQL скрипт для проверки пользователя admin в базе данных
-- Выполните в MySQL Workbench

USE cinema_db;

-- Проверка существования пользователя
SELECT 
    id,
    username,
    LENGTH(password) as password_length,
    SUBSTRING(password, 1, 7) as password_start,
    email,
    full_name,
    role,
    created_at
FROM users 
WHERE username = 'admin';

-- Проверка правильного BCrypt хеша
-- Правильный хеш для пароля "test123" начинается с: $2a$10$
SELECT 
    CASE 
        WHEN password LIKE '$2a$10$%' THEN '✅ Пароль в формате BCrypt'
        ELSE '❌ Пароль НЕ в формате BCrypt'
    END as password_format_check
FROM users 
WHERE username = 'admin';

-- Если пароль неправильный, выполните:
-- UPDATE users SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O' WHERE username = 'admin';

