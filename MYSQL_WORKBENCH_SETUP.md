# Создание пользователей через MySQL Workbench

## Шаг 1: Создайте базу данных (если её нет)

В MySQL Workbench выполните SQL запрос:

```sql
CREATE DATABASE IF NOT EXISTS cinema_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cinema_db;
```

## Шаг 2: Создайте пользователей

Выполните следующий SQL запрос:

```sql
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
```

## Шаг 3: Проверьте создание

```sql
SELECT username, email, role FROM users;
```

Должны увидеть 3 пользователя: admin, cashier, viewer.

## Важно!

⚠️ **Сначала запустите приложение один раз**, чтобы Spring Boot создал таблицы в базе данных!

```bash
mvn spring-boot:run
```

После первого запуска (когда таблицы будут созданы), выполните SQL запросы выше в MySQL Workbench.

## Данные для входа

- **Администратор:** admin / test123
- **Кассир:** cashier / test123  
- **Зритель:** viewer / test123

