package com.github.sddisk.usernotesbackend.handler;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        String message,
        LocalDateTime timestamp,
        Map<String, String> details
) { }