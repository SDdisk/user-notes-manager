package com.github.sddisk.usernotesbackend.service;

import com.github.sddisk.usernotesbackend.store.entity.User;
import com.github.sddisk.usernotesbackend.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        log.info("Saving user: {}", user);
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
