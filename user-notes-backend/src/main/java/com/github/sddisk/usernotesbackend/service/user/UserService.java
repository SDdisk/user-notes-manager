package com.github.sddisk.usernotesbackend.service.user;

import com.github.sddisk.usernotesbackend.store.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User save(User user);

    List<User> getAll();
    User getById(UUID id);

    User getByEmail(String email);

    void deleteById(UUID id);
}