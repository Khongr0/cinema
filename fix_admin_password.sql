-- SQL скрипт для исправления пароля admin
-- Выполните в MySQL Workbench

USE cinema_db;

-- Вариант 1: Обновить существующего admin
-- Пароль будет: test123
UPDATE users 
SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O' 
WHERE username = 'admin';

-- Вариант 2: Удалить и создать заново (если вариант 1 не работает)
-- DELETE FROM users WHERE username = 'admin';
-- 
-- INSERT INTO users (username, password, email, full_name, role, created_at) 
-- VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O', 
--         'admin@cinema.ru', 'Администратор', 'ADMIN', NOW());

-- Проверка
SELECT username, email, role FROM users WHERE username = 'admin';

-- После выполнения войдите: admin / test123

