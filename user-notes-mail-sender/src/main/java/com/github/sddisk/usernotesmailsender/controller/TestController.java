package com.github.sddisk.usernotesmailsender.controller;

import com.github.sddisk.usernotesmailsender.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/send")
@RequiredArgsConstructor
public class TestController {
    private final EmailService emailService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String sendMail(@RequestBody String message) {
        emailService.sendEmail(message);
        return "Send message: " + message;
    }
}
