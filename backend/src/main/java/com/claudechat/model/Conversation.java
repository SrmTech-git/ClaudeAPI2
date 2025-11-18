package com.claudechat.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a conversation with Claude.
 * A conversation contains multiple messages and tracks total token usage.
 */
@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "total_input_tokens")
    private Integer totalInputTokens = 0;

    @Column(name = "total_output_tokens")
    private Integer totalOutputTokens = 0;

    @Column(name = "total_cache_creation_tokens")
    private Integer totalCacheCreationTokens = 0;

    @Column(name = "total_cache_read_tokens")
    private Integer totalCacheReadTokens = 0;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<Message> messages = new ArrayList<>();

    // Constructors
    public Conversation() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public Integer getTotalInputTokens() {
        return totalInputTokens;
    }

    public void setTotalInputTokens(Integer totalInputTokens) {
        this.totalInputTokens = totalInputTokens;
    }

    public Integer getTotalOutputTokens() {
        return totalOutputTokens;
    }

    public void setTotalOutputTokens(Integer totalOutputTokens) {
        this.totalOutputTokens = totalOutputTokens;
    }

    public Integer getTotalCacheCreationTokens() {
        return totalCacheCreationTokens;
    }

    public void setTotalCacheCreationTokens(Integer totalCacheCreationTokens) {
        this.totalCacheCreationTokens = totalCacheCreationTokens;
    }

    public Integer getTotalCacheReadTokens() {
        return totalCacheReadTokens;
    }

    public void setTotalCacheReadTokens(Integer totalCacheReadTokens) {
        this.totalCacheReadTokens = totalCacheReadTokens;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    /**
     * Helper method to add a message to this conversation.
     */
    public void addMessage(Message message) {
        messages.add(message);
        message.setConversation(this);
    }

    /**
     * Helper method to update token counts when a new message is added.
     */
    public void addTokenUsage(int inputTokens, int outputTokens, int cacheCreation, int cacheRead) {
        this.totalInputTokens += inputTokens;
        this.totalOutputTokens += outputTokens;
        this.totalCacheCreationTokens += cacheCreation;
        this.totalCacheReadTokens += cacheRead;
        this.updatedAt = LocalDateTime.now();
    }
}
