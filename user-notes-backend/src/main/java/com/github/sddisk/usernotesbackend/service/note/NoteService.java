package com.github.sddisk.usernotesbackend.service.note;

import com.github.sddisk.usernotesbackend.store.entity.Note;

import java.util.List;
import java.util.UUID;

public interface NoteService {
    Note createUserNote(Note note);

    List<Note> getUserNotes();
    Note getUserNoteById(UUID noteId);

    Note updateUserNote(UUID noteId, Note newNote);
    void deleteUserNoteById(UUID noteId);
}
