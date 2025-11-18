package com.claudechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Main Spring Boot application for Claude Chat Interface.
 * This application provides a REST API for chatting with Claude AI
 * and storing conversation history in PostgreSQL.
 */
@SpringBootApplication
public class ClaudeChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClaudeChatApplication.class, args);
    }

    /**
     * Configure CORS to allow frontend running on localhost:5173 to connect.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(false);
            }
        };
    }
}
