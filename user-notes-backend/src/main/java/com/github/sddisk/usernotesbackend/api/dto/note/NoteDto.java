package com.github.sddisk.usernotesbackend.api.dto.note;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.UUID;

@Builder
public record NoteDto(
        UUID id,
        String title,
        String content,
        @JsonProperty(value = "pinned")
        boolean isPinned
) { }