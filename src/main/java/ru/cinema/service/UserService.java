package ru.cinema.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cinema.model.Role;
import ru.cinema.model.User;
import ru.cinema.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.VIEWER);
        }
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        
        if (!existingUser.getUsername().equals(user.getUsername()) && 
            userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        
        if (!existingUser.getEmail().equals(user.getEmail()) && 
            userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(existingUser.getPassword());
        }
        
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        userRepository.deleteById(id);
    }

    public List<User> searchUsers(String keyword) {
        return userRepository.findByFullNameContainingIgnoreCase(keyword);
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public long getUserCount() {
        return userRepository.count();
    }
}

