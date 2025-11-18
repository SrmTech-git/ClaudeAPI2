package com.claudechat.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a single message in a conversation.
 * Messages can be from the user or from Claude (assistant).
 * Claude's messages may include thinking blocks.
 */
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @Column(name = "role", nullable = false)
    private String role; // "user" or "assistant"

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "thinking", columnDefinition = "TEXT")
    private String thinking; // Extended thinking content (if present)

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "input_tokens")
    private Integer inputTokens;

    @Column(name = "output_tokens")
    private Integer outputTokens;

    @Column(name = "cache_creation_tokens")
    private Integer cacheCreationTokens;

    @Column(name = "cache_read_tokens")
    private Integer cacheReadTokens;

    // Constructors
    public Message() {
        this.createdAt = LocalDateTime.now();
    }

    public Message(String role, String content) {
        this();
        this.role = role;
        this.content = content;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
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
