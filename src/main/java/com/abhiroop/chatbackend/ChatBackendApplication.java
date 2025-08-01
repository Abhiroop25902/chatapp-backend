package com.abhiroop.chatbackend;

import com.abhiroop.chatbackend.config.JwtConfig;
import com.microsoft.applicationinsights.attach.ApplicationInsights;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({JwtConfig.class})
@SpringBootApplication
public class ChatBackendApplication {

    public static void main(String[] args) {
        ApplicationInsights.attach();
        SpringApplication.run(ChatBackendApplication.class, args);
    }

}
