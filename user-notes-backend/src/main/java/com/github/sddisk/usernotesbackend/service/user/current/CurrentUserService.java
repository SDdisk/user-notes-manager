package com.github.sddisk.usernotesbackend.service.user.current;

import com.github.sddisk.usernotesbackend.store.entity.User;

import java.util.UUID;

public interface CurrentUserService {
    UUID getCurrentUserId();
    User getCurrentUser();
}
