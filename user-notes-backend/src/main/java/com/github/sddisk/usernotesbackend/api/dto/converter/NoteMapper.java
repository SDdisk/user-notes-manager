package com.github.sddisk.usernotesbackend.api.dto.converter;

import com.github.sddisk.usernotesbackend.api.dto.note.NoteDto;
import com.github.sddisk.usernotesbackend.store.entity.Note;

public abstract class NoteMapper {
    public static NoteDto toDto(Note note) {
        return NoteDto.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .isPinned(note.isPinned())
                .build();
    }

    public static Note toNote(NoteDto dto) {
        return Note.builder()
                .id(dto.id())
                .title(dto.title())
                .content(dto.content())
                .isPinned(dto.isPinned())
                .build();
    }
}
