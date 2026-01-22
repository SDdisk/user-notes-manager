package com.github.sddisk.usernotesbackend.handler;


import com.github.sddisk.usernotesbackend.exception.NoteNotFoundException;
import com.github.sddisk.usernotesbackend.exception.UserAlreadyExistException;
import com.github.sddisk.usernotesbackend.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.time.LocalDateTime.now;

@RestControllerAdvice @Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        var bindingResult = ex.getBindingResult();
        Map<String, String> details = new HashMap<>();

        bindingResult.getFieldErrors().forEach(error ->
                details.put(error.getField(), error.getDefaultMessage()));

        return new ErrorResponse(
                "Validation failed",
                now(),
                details
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoteNotFoundException.class)
    public ErrorResponse handleNoteNotFound(NoteNotFoundException ex) {
        return new ErrorResponse(
                ex.getMessage(),
                now(),
                Collections.emptyMap()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistException.class)
    public ErrorResponse handleUserAlreadyExist(UserAlreadyExistException ex) {
        return new ErrorResponse(
                ex.getMessage(),
                now(),
                Collections.emptyMap()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound(UserNotFoundException ex) {
        return new ErrorResponse(
                ex.getMessage(),
                now(),
                Collections.emptyMap()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestCookieException.class)
    public ErrorResponse handleUserNotFound(MissingRequestCookieException ex) {
        return new ErrorResponse(
                ex.getMessage(),
                now(),
                Collections.emptyMap()
        );
    }
}