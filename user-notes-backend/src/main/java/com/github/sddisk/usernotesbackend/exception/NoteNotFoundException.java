package com.github.sddisk.usernotesbackend.exception;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException() {
        super("Note not found");
    }

    public NoteNotFoundException(String message) {
        super(message);
    }
}
