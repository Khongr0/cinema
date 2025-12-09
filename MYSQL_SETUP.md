# Настройка MySQL для проекта

## Шаг 1: Убедитесь, что MySQL установлен и запущен

### macOS (через Homebrew):
```bash
# Проверить статус
brew services list | grep mysql

# Если не запущен, запустить:
brew services start mysql
# или
mysql.server start
```

### Linux:
```bash
sudo systemctl status mysql
# Если не запущен:
sudo systemctl start mysql
```

## Шаг 2: Создайте базу данных

Подключитесь к MySQL:
```bash
mysql -u root -p
```

Выполните SQL:
```sql
CREATE DATABASE IF NOT EXISTS cinema_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EXIT;
```

## Шаг 3: Настройте application.properties

Откройте файл `src/main/resources/application.properties` и проверьте настройки:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cinema_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=ваш_пароль_mysql
```

**Важно:** Замените `root` на ваш пароль MySQL, если он отличается.

## Шаг 4: Создайте тестовых пользователей

После первого запуска приложения выполните SQL для создания пользователей:

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

## Шаг 5: Запустите приложение

```bash
mvn spring-boot:run
```

Приложение автоматически создаст таблицы при первом запуске.

## Данные для входа

- **Администратор:** admin / test123
- **Кассир:** cashier / test123
- **Зритель:** viewer / test123

