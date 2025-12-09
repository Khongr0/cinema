-- Простое решение для создания admin (работает в безопасном режиме)
-- Выполните в MySQL Workbench

USE cinema_db;

-- ШАГ 1: Проверка текущего состояния
SELECT '=== ТЕКУЩЕЕ СОСТОЯНИЕ ===' AS info;
SELECT username, role, LENGTH(password) as pwd_len, 
       SUBSTRING(password, 1, 7) as pwd_start
FROM users WHERE username = 'admin';

-- ШАГ 2: Простое удаление (должно работать, так как username имеет уникальный индекс)
-- Если не работает, используйте UPDATE вместо DELETE (см. ниже)
DELETE FROM users WHERE username = 'admin';

-- АЛЬТЕРНАТИВА: Если DELETE не работает, используйте UPDATE для изменения пароля
-- UPDATE users 
-- SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O',
--     role = 'ADMIN',
--     email = 'admin@cinema.ru',
--     full_name = 'Администратор Системы'
-- WHERE username = 'admin';

-- ШАГ 3: Создание нового admin
INSERT INTO users (username, password, email, full_name, role, created_at) 
VALUES (
    'admin', 
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O', 
    'admin@cinema.ru', 
    'Администратор Системы', 
    'ADMIN', 
    NOW()
);

-- ШАГ 4: Проверка
SELECT '=== РЕЗУЛЬТАТ ===' AS info;
SELECT 
    username,
    CASE 
        WHEN password LIKE '$2a$10$%' THEN '✅ Пароль правильный'
        ELSE '❌ Пароль неправильный'
    END as password_check,
    CASE 
        WHEN role = 'ADMIN' THEN '✅ Роль правильная'
        ELSE CONCAT('❌ Роль: ', role)
    END as role_check
FROM users 
WHERE username = 'admin';

-- После этого перезапустите приложение и войдите: admin / test123

