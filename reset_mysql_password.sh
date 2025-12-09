#!/bin/bash
# Скрипт для сброса пароля MySQL на macOS

echo "🔧 Сброс пароля MySQL"
echo ""
echo "ВНИМАНИЕ: Этот скрипт остановит MySQL и запустит в безопасном режиме"
echo ""

read -p "Введите новый пароль для root: " NEW_PASSWORD

echo ""
echo "1. Останавливаю MySQL..."
brew services stop mysql 2>/dev/null || sudo /usr/local/mysql/support-files/mysql.server stop

echo "2. Запускаю MySQL в безопасном режиме..."
sudo mysqld_safe --skip-grant-tables --skip-networking &

sleep 3

echo "3. Сбрасываю пароль..."
mysql -u root << EOF
FLUSH PRIVILEGES;
ALTER USER 'root'@'localhost' IDENTIFIED BY '$NEW_PASSWORD';
FLUSH PRIVILEGES;
EXIT;
EOF

echo "4. Останавливаю безопасный режим..."
sudo pkill mysqld_safe
sudo pkill mysqld

echo "5. Запускаю MySQL в обычном режиме..."
brew services start mysql

sleep 3

echo ""
echo "✅ Пароль изменен!"
echo "Новый пароль: $NEW_PASSWORD"
echo ""
echo "Обновите application.properties:"
echo "spring.datasource.password=$NEW_PASSWORD"

