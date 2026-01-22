package com.github.sddisk.usernotesbackend.service.kafka;

import com.github.sddisk.usernotesbackend.api.dto.email.EmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaEmailService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC_USER_REGISTERED = "user-registered";

    public void sendWelcomeEmail(String email, String username) {
        String subject = "Welcome to your notes!";
        String text = String.format("Thank you %s for creating account!", username);

        EmailDto emailDto = new EmailDto(email, subject, text);

        String message = objectMapper.writeValueAsString(emailDto);

        log.info("Send message to kafka: {}", message);
        kafkaTemplate.send(TOPIC_USER_REGISTERED, message);
    }
}