# Решение проблемы с паролем MySQL

## Проблема: Access denied for user 'root'@'localhost'

### Решение 1: Сброс пароля через скрипт (macOS)

```bash
./reset_mysql_password.sh
```

Скрипт попросит ввести новый пароль и автоматически сбросит старый.

### Решение 2: Сброс пароля вручную

1. **Остановите MySQL:**
   ```bash
   brew services stop mysql
   ```

2. **Запустите MySQL в безопасном режиме:**
   ```bash
   sudo mysqld_safe --skip-grant-tables --skip-networking &
   ```

3. **Подключитесь БЕЗ пароля:**
   ```bash
   mysql -u root
   ```

4. **Сбросьте пароль:**
   ```sql
   FLUSH PRIVILEGES;
   ALTER USER 'root'@'localhost' IDENTIFIED BY 'новый_пароль';
   FLUSH PRIVILEGES;
   EXIT;
   ```

5. **Остановите безопасный режим:**
   ```bash
   sudo pkill mysqld_safe
   sudo pkill mysqld
   ```

6. **Запустите MySQL нормально:**
   ```bash
   brew services start mysql
   ```

7. **Обновите application.properties:**
   ```properties
   spring.datasource.password=новый_пароль
   ```

### Решение 3: Использовать socket authentication (macOS)

На macOS можно использовать socket authentication:

1. **Измените application.properties:**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/cinema_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=
   ```

2. **Попробуйте подключиться:**
   ```bash
   mysql -u root
   ```

### Решение 4: Создать нового пользователя

Если не удается сбросить пароль root, создайте нового пользователя:

1. **Подключитесь через sudo (если возможно):**
   ```bash
   sudo mysql -u root
   ```

2. **Создайте нового пользователя:**
   ```sql
   CREATE USER 'cinema_user'@'localhost' IDENTIFIED BY 'cinema_password';
   GRANT ALL PRIVILEGES ON cinema_db.* TO 'cinema_user'@'localhost';
   FLUSH PRIVILEGES;
   EXIT;
   ```

3. **Обновите application.properties:**
   ```properties
   spring.datasource.username=cinema_user
   spring.datasource.password=cinema_password
   ```

## Проверка подключения

После настройки проверьте подключение:

```bash
mysql -u root -p
# или
mysql -u cinema_user -p
```

## Создание базы данных

После успешного подключения:

```sql
CREATE DATABASE IF NOT EXISTS cinema_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EXIT;
```

