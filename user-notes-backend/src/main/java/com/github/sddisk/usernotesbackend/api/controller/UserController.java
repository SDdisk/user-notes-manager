package com.github.sddisk.usernotesbackend.api.controller;

import com.github.sddisk.usernotesbackend.api.dto.converter.UserMapper;
import com.github.sddisk.usernotesbackend.api.dto.user.UserResponseDto;
import com.github.sddisk.usernotesbackend.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto current(@AuthenticationPrincipal(expression = "username") String username) {
        return UserMapper.toDto(userService.getByEmail(username));
    }

    @GetMapping("/public")
    @ResponseStatus(HttpStatus.OK)
    public String publicMethod() {
        return "success";
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getAll() {
        return userService.getAll().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getById(@PathVariable UUID id) {
        var user = userService.getById(id);
        return UserMapper.toDto(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id) {
        userService.deleteById(id);
    }
}