package com.github.sddisk.usernotesbackend.service;

import com.github.sddisk.usernotesbackend.store.entity.User;
import com.github.sddisk.usernotesbackend.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        LOGGER.info("Saving user: {}", user);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        LOGGER.info("Fetch all users");
        return userRepository.findAll();
    }

    @Override
    public User getById(UUID id) {
        LOGGER.info("Fetch user with id: {}", id);
        return userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("User not found with id " + id)
        );
    }

    @Override
    public void deleteById(UUID id) {
        LOGGER.info("Deleting user with id: {}", id);
        userRepository.deleteById(id);
    }
}
