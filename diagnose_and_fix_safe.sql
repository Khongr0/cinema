-- Полная диагностика и исправление пользователя admin (безопасный режим)
-- Выполните в MySQL Workbench

USE cinema_db;

-- ШАГ 1: Проверка текущего состояния
SELECT '=== ТЕКУЩЕЕ СОСТОЯНИЕ ===' AS info;
SELECT 
    id,
    username,
    LENGTH(username) as username_len,
    LENGTH(password) as password_len,
    SUBSTRING(password, 1, 10) as password_start,
    email,
    full_name,
    role,
    LENGTH(role) as role_len
FROM users 
WHERE username = 'admin' OR username LIKE '%admin%';

-- ШАГ 2: Удаление всех вариантов admin (безопасный способ)
SELECT '=== УДАЛЕНИЕ СТАРЫХ ЗАПИСЕЙ ===' AS info;
-- Сначала найдем ID всех записей admin
SELECT id INTO @admin_id FROM users WHERE username = 'admin' LIMIT 1;
DELETE FROM users WHERE id = @admin_id;

-- Удаляем записи с пробелами (если есть)
DELETE FROM users WHERE id IN (
    SELECT id FROM (
        SELECT id FROM users WHERE TRIM(username) = 'admin'
    ) AS temp
);

-- ШАГ 3: Создание правильного admin
SELECT '=== СОЗДАНИЕ НОВОГО ADMIN ===' AS info;
INSERT INTO users (username, password, email, full_name, role, created_at) 
VALUES (
    'admin',  -- точно 'admin' без пробелов
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O',  -- BCrypt хеш для 'test123'
    'admin@cinema.ru', 
    'Администратор Системы', 
    'ADMIN',  -- обязательно заглавными буквами
    NOW()
);

-- ШАГ 4: Финальная проверка
SELECT '=== ФИНАЛЬНАЯ ПРОВЕРКА ===' AS info;
SELECT 
    username,
    CASE 
        WHEN password LIKE '$2a$10$%' THEN '✅ Пароль BCrypt правильный'
        ELSE '❌ Пароль неправильный'
    END as password_check,
    CASE 
        WHEN role = 'ADMIN' THEN '✅ Роль правильная'
        ELSE CONCAT('❌ Роль неправильная: ', role)
    END as role_check,
    email,
    full_name
FROM users 
WHERE username = 'admin';

-- Если все правильно, вы увидите:
-- username: admin
-- password_check: ✅ Пароль BCrypt правильный
-- role_check: ✅ Роль правильная

-- После этого перезапустите приложение и войдите: admin / test123

