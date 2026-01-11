package com.github.sddisk.usernotesbackend.service.user;

import com.github.sddisk.usernotesbackend.store.entity.User;
import com.github.sddisk.usernotesbackend.store.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        log.info("Saving user: {}", user);

        if (userRepository.existsUserByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + "already exists"); // todo custom exception
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        log.info("Fetch all users");
        return userRepository.findAll();
    }

    @Override
    public User getById(UUID id) {
        log.info("Fetch user with id: {}", id);
        return userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("User not found with id " + id)
        );
    }

    @Override
    public void deleteById(UUID id) {
        log.info("Deleting user with id: {}", id);
        userRepository.deleteById(id);
    }
}
