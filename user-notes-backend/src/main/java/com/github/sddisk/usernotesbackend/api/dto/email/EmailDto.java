package com.github.sddisk.usernotesbackend.api.dto.email;

public record EmailDto(
        String to,
        String subject,
        String text
) { }