package com.github.sddisk.usernotesbackend.service.note;

import com.github.sddisk.usernotesbackend.service.user.UserService;
import com.github.sddisk.usernotesbackend.service.user.current.CurrentUserService;
import com.github.sddisk.usernotesbackend.store.entity.Note;
import com.github.sddisk.usernotesbackend.store.entity.User;
import com.github.sddisk.usernotesbackend.store.repository.NoteRepository;
import com.github.sddisk.usernotesbackend.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteServiceImpl implements NoteService {
    private final CurrentUserService currentUserService;
    private final NoteRepository noteRepository;
    private final UserService userService;

    @Override
    public Note createUserNote(Note note) {
        log.info("Saving note {}", note);

        var currentUserId = currentUserService.getCurrentUserId();
        logCurrentUserId(currentUserId);

        var currentUser = userService.getById(currentUserId);

        note.setUser(currentUser);
        log.debug("Current user set for note {}", note);

        log.info("Note saved");
        return noteRepository.save(note);
    }

    @Override
    public List<Note> getUserNotes() {
        log.info("Get user notes");

        var currentUserId = currentUserService.getCurrentUserId();
        logCurrentUserId(currentUserId);

        var currentUser = userService.getById(currentUserId);

        return currentUser.getNotes().stream().toList();
    }

    @Override
    public Note getUserNoteById(UUID noteId) {
        log.info("Get user note by id {}", noteId);

        var currentUserId = currentUserService.getCurrentUserId();
        logCurrentUserId(currentUserId);

        var currentUser = userService.getById(currentUserId);

        return currentUser.getNotes().stream()
                .filter(note -> note.getId().equals(noteId))
                .findFirst()
                .orElseThrow(
                        () ->  new IllegalArgumentException("User " + currentUser + "dont have a note with id " + noteId)
                );
    }

    @Override
    public Note updateUserNote(UUID noteId, Note newNote) {
        log.info("Update note with id {}", noteId);
        log.debug("New note {}", newNote);

        var note = getUserNoteById(noteId);
        log.debug("Note before {}", note);

        updateNote(note, newNote);
        log.debug("Note after {}", note);

        log.info("Note updated");
        return noteRepository.save(note);
    }

    @Override
    @Transactional
    public void deleteUserNoteById(UUID noteId) {
        log.info("Delete note with id {}", noteId);

        var currentUserId = currentUserService.getCurrentUserId();
        logCurrentUserId(currentUserId);

        var currentUser = userService.getById(currentUserId);

        var note = currentUser.getNotes().stream()
                .filter(n -> n.getId().equals(noteId))
                .findFirst();

        if (note.isEmpty()) {
            log.error("Note with id {} not exist in user {}", noteId, currentUser);
            throw new IllegalArgumentException("User " + currentUser + "dont have a note with id " + noteId);
        }

        log.info("Note deleted");
        currentUser.getNotes().remove(note.get());
        noteRepository.deleteById(noteId);
    }

    private void updateNote(Note toUpdate, Note newNote) {
        toUpdate.setTitle(newNote.getTitle());
        toUpdate.setContent(newNote.getContent());
        toUpdate.setPinned(newNote.isPinned());
    }

    private void logCurrentUserId(UUID id) {
        log.debug("Current user id {}", id);
    }
}
