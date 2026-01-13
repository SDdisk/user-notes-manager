package com.github.sddisk.usernotesbackend.api.controller;

import com.github.sddisk.usernotesbackend.api.dto.note.NoteDto;
import com.github.sddisk.usernotesbackend.api.dto.converter.NoteMapper;
import com.github.sddisk.usernotesbackend.service.note.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<NoteDto> getAllNotes() {
        return noteService.getUserNotes().stream()
                .map(NoteMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NoteDto getNoteById(@PathVariable("id") UUID noteId) {
        return NoteMapper.toDto(noteService.getUserNoteById(noteId));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public NoteDto createNote(@RequestBody NoteDto noteDto) {
        var createdNote = noteService.createUserNote(
                NoteMapper.toNote(noteDto)
        );

        return NoteMapper.toDto(createdNote);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NoteDto updateNote(@PathVariable("id") UUID noteId, @RequestBody NoteDto newNoteDto) {
        var updatedNote = noteService.updateUserNote(
                noteId,
                NoteMapper.toNote(newNoteDto)
        );

        return NoteMapper.toDto(updatedNote);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable("id") UUID noteId) {
        noteService.deleteUserNoteById(noteId);
    }

}
