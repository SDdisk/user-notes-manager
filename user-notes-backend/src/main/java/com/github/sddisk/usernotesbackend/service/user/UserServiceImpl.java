package com.github.sddisk.usernotesbackend.service.user;

import com.github.sddisk.usernotesbackend.exception.UserAlreadyExistException;
import com.github.sddisk.usernotesbackend.exception.UserNotFoundException;
import com.github.sddisk.usernotesbackend.store.entity.User;
import com.github.sddisk.usernotesbackend.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            throw new UserAlreadyExistException("User with email " + user.getEmail() + "already exists");
        }

        hashPassword(user);
        setRoleUser(user);

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
                () -> new UserNotFoundException("User not found with id " + id)
        );
    }

    @Override
    public User getByEmail(String email) {
        log.info("Fetch user with email: {}", email);
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User not found with email " + email)
        );
    }

    @Override
    public void deleteById(UUID id) {
        log.info("Deleting user with id: {}", id);
        userRepository.deleteById(id);
    }

    private void hashPassword(User user) {
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );
    }

    private void setRoleUser(User user) {
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        user.setRoles(roles);
        //user.getRoles().add("USER");
    }
}
