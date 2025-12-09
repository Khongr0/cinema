package ru.cinema.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.cinema.model.Role;
import ru.cinema.model.User;
import ru.cinema.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Создаем администратора, если его нет
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("test123")); // Пароль будет автоматически зашифрован
            admin.setEmail("admin@cinema.ru");
            admin.setFullName("Администратор Системы");
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("✅ Пользователь admin создан автоматически (пароль: test123)");
        } else {
            // Обновляем пароль существующего admin на правильный
            userRepository.findByUsername("admin").ifPresent(user -> {
                String correctPasswordHash = passwordEncoder.encode("test123");
                user.setPassword(correctPasswordHash);
                user.setRole(Role.ADMIN); // Убеждаемся, что роль правильная
                user.setEmail("admin@cinema.ru");
                user.setFullName("Администратор Системы");
                userRepository.save(user);
                System.out.println("✅ Пароль пользователя admin обновлен (пароль: test123)");
            });
        }

        // Создаем кассира, если его нет
        if (!userRepository.existsByUsername("cashier")) {
            User cashier = new User();
            cashier.setUsername("cashier");
            cashier.setPassword(passwordEncoder.encode("test123"));
            cashier.setEmail("cashier@cinema.ru");
            cashier.setFullName("Кассир Иванов Иван");
            cashier.setRole(Role.CASHIER);
            userRepository.save(cashier);
            System.out.println("✅ Пользователь cashier создан автоматически (пароль: test123)");
        }

        // Создаем зрителя, если его нет
        if (!userRepository.existsByUsername("viewer")) {
            User viewer = new User();
            viewer.setUsername("viewer");
            viewer.setPassword(passwordEncoder.encode("test123"));
            viewer.setEmail("viewer@cinema.ru");
            viewer.setFullName("Зритель Петров Петр");
            viewer.setRole(Role.VIEWER);
            userRepository.save(viewer);
            System.out.println("✅ Пользователь viewer создан автоматически (пароль: test123)");
        }
    }
}

