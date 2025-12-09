# Быстрый старт

## Проблема: Whitelabel Error Page

Если вы видите ошибку "This application has no explicit mapping for /error", это обычно означает, что:

1. **Приложение не запустилось полностью** - возможно, проблема с подключением к БД
2. **MySQL не запущен или не настроен**

## Решение 1: Использовать H2 (встроенная БД для тестирования)

Временно переименуйте файлы:
```bash
mv src/main/resources/application.properties src/main/resources/application-mysql.properties
mv src/main/resources/application-h2.properties src/main/resources/application.properties
```

Затем запустите:
```bash
mvn spring-boot:run
```

## Решение 2: Настроить MySQL

1. Убедитесь, что MySQL запущен:
```bash
# macOS
brew services start mysql
# или
mysql.server start

# Linux
sudo systemctl start mysql
```

2. Создайте базу данных:
```sql
CREATE DATABASE cinema_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. Проверьте настройки в `application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=ваш_пароль
```

4. Запустите приложение:
```bash
mvn spring-boot:run
```

## Проверка работы

После запуска откройте в браузере:
- `http://localhost:8080` - главная страница
- `http://localhost:8080/login` - страница входа
- `http://localhost:8080/register` - регистрация

Если используете H2, консоль БД доступна по адресу:
- `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:cinema_db`
- Username: `sa`
- Password: (пусто)

## Создание первого пользователя

После запуска с H2 или MySQL, зарегистрируйте пользователя через `/register` или создайте администратора через SQL:

```sql
-- Пароль: admin123
INSERT INTO users (username, password, email, full_name, role, created_at) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pQ0O', 
        'admin@cinema.ru', 'Администратор', 'ADMIN', NOW());
```

