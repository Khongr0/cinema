package ru.cinema.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Публичные страницы - доступны всем
                .requestMatchers("/", "/home", "/login", "/register", "/about", 
                               "/css/**", "/js/**", "/images/**", "/error").permitAll()
                
                // Админские страницы - только для ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Кассирские страницы - для CASHIER и ADMIN
                .requestMatchers("/cashier/**").hasAnyRole("CASHIER", "ADMIN")
                
                // ========== ФОРМЫ СОЗДАНИЯ И РЕДАКТИРОВАНИЯ ==========
                // Только для CASHIER и ADMIN
                .requestMatchers(HttpMethod.GET, "/movies/new").hasAnyRole("CASHIER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/movies/*/edit").hasAnyRole("CASHIER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/sessions/new").hasAnyRole("CASHIER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/sessions/*/edit").hasAnyRole("CASHIER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/halls/new").hasAnyRole("CASHIER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/halls/*/edit").hasAnyRole("CASHIER", "ADMIN")
                
                // ========== POST ЗАПРОСЫ ДЛЯ СОЗДАНИЯ/ОБНОВЛЕНИЯ/УДАЛЕНИЯ ==========
                // Только для CASHIER и ADMIN
                .requestMatchers(HttpMethod.POST, "/movies").hasAnyRole("CASHIER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/movies/*").hasAnyRole("CASHIER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/movies/*/delete").hasAnyRole("CASHIER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/sessions").hasAnyRole("CASHIER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/sessions/*").hasAnyRole("CASHIER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/sessions/*/delete").hasAnyRole("CASHIER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/halls").hasAnyRole("CASHIER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/halls/*").hasAnyRole("CASHIER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/halls/*/delete").hasAnyRole("CASHIER", "ADMIN")
                
                // ========== GET ЗАПРОСЫ ДЛЯ ПРОСМОТРА ==========
                // Доступны ВСЕМ аутентифицированным пользователям (включая VIEWER)
                // Важно: эти правила должны быть ПОСЛЕ более специфичных правил выше
                .requestMatchers(HttpMethod.GET, "/movies").authenticated()
                .requestMatchers(HttpMethod.GET, "/movies/{id}").authenticated()
                .requestMatchers(HttpMethod.GET, "/sessions").authenticated()
                .requestMatchers(HttpMethod.GET, "/sessions/{id}").authenticated()
                .requestMatchers(HttpMethod.GET, "/halls").authenticated()
                .requestMatchers(HttpMethod.GET, "/halls/{id}").authenticated()
                .requestMatchers(HttpMethod.GET, "/tickets").authenticated()
                .requestMatchers(HttpMethod.GET, "/tickets/{id}").authenticated()
                .requestMatchers("/statistics").authenticated()
                
                // Все остальные запросы требуют аутентификации
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()); // Для упрощения, в продакшене лучше включить

        return http.build();
    }
}
