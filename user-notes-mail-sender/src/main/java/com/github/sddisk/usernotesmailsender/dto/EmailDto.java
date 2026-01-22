package com.github.sddisk.usernotesmailsender.dto;

public record EmailDto(
        String to,
        String subject,
        String text
) { }