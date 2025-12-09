# Создание администратора

Если вы не можете войти как admin, выполните одно из следующих действий:

## Способ 1: Через H2 консоль (рекомендуется)

1. Откройте в браузере: http://localhost:8080/h2-console
2. Введите настройки подключения:
   - JDBC URL: `jdbc:h2:mem:cinema_db`
   - User Name: `sa`
   - Password: (оставьте пустым)
3. Нажмите "Connect"
4. Выполните SQL запрос:

```sql
-- Удалить существующего admin, если есть
DELETE FROM users WHERE username = 'admin';

-- Создать нового администратора
-- Пароль: test123
INSERT INTO users (username, password, email, full_name, role, created_at) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O', 
        'admin@cinema.ru', 'Администратор Системы', 'ADMIN', CURRENT_TIMESTAMP);
```

5. Войдите с данными: admin / test123

## Способ 2: Через регистрацию + изменение роли

1. Зарегистрируйте нового пользователя: http://localhost:8080/register
2. Запомните имя пользователя
3. Откройте H2 консоль: http://localhost:8080/h2-console
4. Выполните SQL:

```sql
-- Замените 'ваше_имя' на имя, которое вы использовали при регистрации
UPDATE users SET role = 'ADMIN' WHERE username = 'ваше_имя';
```

5. Войдите с вашими данными

