package com.github.sddisk.usernotesmailsender.service;

import com.github.sddisk.usernotesmailsender.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final MailSender mailSender;
    private final ObjectMapper objectMapper;


    public void sendEmail(String message) {
        log.info("Sending message: {}", message);

        EmailDto emailDto = objectMapper.readValue(message, EmailDto.class);
        log.debug("Message converted to email dto: {}", emailDto);

        var simpleMessage = new SimpleMailMessage();
        simpleMessage.setFrom("no-reply");
        simpleMessage.setTo(emailDto.to());
        simpleMessage.setSubject(emailDto.subject());
        simpleMessage.setText(emailDto.text());
        log.info("Created simple message: {}", simpleMessage);

        mailSender.send(simpleMessage);
        log.info("Mail send to {}", emailDto.to());
    }
}
