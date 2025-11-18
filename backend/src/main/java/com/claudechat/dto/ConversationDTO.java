package com.claudechat.dto;

import com.claudechat.model.Conversation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for transferring conversation data to the frontend.
 */
public class ConversationDTO {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int totalInputTokens;
    private int totalOutputTokens;
    private int totalCacheCreationTokens;
    private int totalCacheReadTokens;
    private List<MessageDTO> messages;

    // Constructors
    public ConversationDTO() {
    }

    /**
     * Create a DTO from a Conversation entity.
     */
    public static ConversationDTO fromEntity(Conversation conversation) {
        ConversationDTO dto = new ConversationDTO();
        dto.setId(conversation.getId());
        dto.setCreatedAt(conversation.getCreatedAt());
        dto.setUpdatedAt(conversation.getUpdatedAt());
        dto.setTotalInputTokens(conversation.getTotalInputTokens());
        dto.setTotalOutputTokens(conversation.getTotalOutputTokens());
        dto.setTotalCacheCreationTokens(conversation.getTotalCacheCreationTokens());
        dto.setTotalCacheReadTokens(conversation.getTotalCacheReadTokens());
        dto.setMessages(conversation.getMessages().stream()
                .map(MessageDTO::fromEntity)
                .collect(Collectors.toList()));
        return dto;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getTotalInputTokens() {
        return totalInputTokens;
    }

    public void setTotalInputTokens(int totalInputTokens) {
        this.totalInputTokens = totalInputTokens;
    }

    public int getTotalOutputTokens() {
        return totalOutputTokens;
    }

    public void setTotalOutputTokens(int totalOutputTokens) {
        this.totalOutputTokens = totalOutputTokens;
    }

    public int getTotalCacheCreationTokens() {
        return totalCacheCreationTokens;
    }

    public void setTotalCacheCreationTokens(int totalCacheCreationTokens) {
        this.totalCacheCreationTokens = totalCacheCreationTokens;
    }

    public int getTotalCacheReadTokens() {
        return totalCacheReadTokens;
    }

    public void setTotalCacheReadTokens(int totalCacheReadTokens) {
        this.totalCacheReadTokens = totalCacheReadTokens;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }
}
