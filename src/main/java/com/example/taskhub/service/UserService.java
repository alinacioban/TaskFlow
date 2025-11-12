package com.example.taskhub.service;

import com.example.taskhub.domain.Role;
import com.example.taskhub.domain.User;
import com.example.taskhub.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @PostConstruct
    @Transactional
    public void createDemoUsers() {
        if (userRepository.count() > 0) {
            return;
        }
        User user = User.builder()
                .username("user")
                .fullName("Demo User")
                .passwordHash(passwordEncoder.encode("password"))
                .roles(Set.of(Role.USER))
                .build();
        User admin = User.builder()
                .username("admin")
                .fullName("Administrator")
                .passwordHash(passwordEncoder.encode("admin"))
                .roles(Set.of(Role.USER, Role.ADMIN))
                .build();
        userRepository.save(user);
        userRepository.save(admin);
    }
}
