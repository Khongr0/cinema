#!/bin/bash
# Скрипт для запуска приложения с H2 (встроенная БД)

echo "Переключение на H2 базу данных..."

# Создаем резервную копию MySQL конфигурации
cp src/main/resources/application.properties src/main/resources/application-mysql.properties.backup

# Заменяем на H2 конфигурацию
cat > src/main/resources/application.properties << 'EOF'
# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:cinema_db
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
spring.jpa.properties.hibernate.format_sql=true

# Thymeleaf Configuration
spring.thymeleaf.cache=false
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.mode=HTML

# Server Configuration
server.port=8080
server.servlet.encoding.charset=UTF-8
server.servlet.encoding.enabled=true
server.servlet.encoding.force=true

# Logging
logging.level.org.springframework.web=INFO
logging.level.org.hibernate=INFO
EOF

echo "Конфигурация обновлена. Запуск приложения..."
mvn spring-boot:run

