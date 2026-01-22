package com.github.sddisk.usernotesmailsender.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfiguration {

    @Bean
    public NewTopic createTopic() {
        return TopicBuilder
                .name("user-registered")
                .partitions(3)
                .build();
    }
    
}
