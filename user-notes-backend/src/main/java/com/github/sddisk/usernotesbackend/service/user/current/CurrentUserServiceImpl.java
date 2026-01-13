package com.github.sddisk.usernotesbackend.service.user.current;

import com.github.sddisk.usernotesbackend.security.user.adapter.UserDetailsAdapter;
import com.github.sddisk.usernotesbackend.store.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {

    @Override
    public UUID getCurrentUserId() {
        Authentication authentication = getAuthentication();
        validateAuthentication(authentication);
        return extractId(authentication);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = getAuthentication();
        validateAuthentication(authentication);
        return extractUser(authentication);
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private void validateAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user");
        }
    }

    private UUID extractId(Authentication authentication) {
        var principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsAdapter) {
            return ((UserDetailsAdapter) principal).getId();
        }
        throw new IllegalStateException("Unexpected principal type");
    }

    private User extractUser(Authentication authentication) {
        var principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsAdapter) {
            return ((UserDetailsAdapter) principal).getUser();
        }
        throw new IllegalStateException("Unexpected principal type");
    }
}
