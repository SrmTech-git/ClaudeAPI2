package com.claudechat.dto;

import com.claudechat.model.Message;
import java.time.LocalDateTime;

/**
 * DTO for transferring message data to the frontend.
 */
public class MessageDTO {

    private Long id;
    private String role;
    private String content;
    private String thinking;
    private LocalDateTime createdAt;
    private Integer inputTokens;
    private Integer outputTokens;
    private Integer cacheCreationTokens;
    private Integer cacheReadTokens;

    // Constructors
    public MessageDTO() {
    }

    /**
     * Create a DTO from a Message entity.
     */
    public static MessageDTO fromEntity(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setRole(message.getRole());
        dto.setContent(message.getContent());
        dto.setThinking(message.getThinking());
        dto.setCreatedAt(message.getCreatedAt());
        dto.setInputTokens(message.getInputTokens());
        dto.setOutputTokens(message.getOutputTokens());
        dto.setCacheCreationTokens(message.getCacheCreationTokens());
        dto.setCacheReadTokens(message.getCacheReadTokens());
        return dto;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThinking() {
        return thinking;
    }

    public void setThinking(String thinking) {
        this.thinking = thinking;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getInputTokens() {
        return inputTokens;
    }

    public void setInputTokens(Integer inputTokens) {
        this.inputTokens = inputTokens;
    }

    public Integer getOutputTokens() {
        return outputTokens;
    }

    public void setOutputTokens(Integer outputTokens) {
        this.outputTokens = outputTokens;
    }

    public Integer getCacheCreationTokens() {
        return cacheCreationTokens;
    }

    public void setCacheCreationTokens(Integer cacheCreationTokens) {
        this.cacheCreationTokens = cacheCreationTokens;
    }

    public Integer getCacheReadTokens() {
        return cacheReadTokens;
    }

    public void setCacheReadTokens(Integer cacheReadTokens) {
        this.cacheReadTokens = cacheReadTokens;
    }
}
