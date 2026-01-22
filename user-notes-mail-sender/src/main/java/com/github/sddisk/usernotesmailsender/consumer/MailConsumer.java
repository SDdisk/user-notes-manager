package com.github.sddisk.usernotesmailsender.consumer;

import com.github.sddisk.usernotesmailsender.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = {"user-registered"}, concurrency = "3")
    public void sendEmail(String message) {
        log.info("Read message from topic user-registered: {}", message);
        emailService.sendEmail(message);
    }
}
