package com.claudechat.dto;

/**
 * Request object for sending a chat message.
 * Contains the user's message and optionally a conversation ID to continue.
 */
public class ChatRequest {

    private Long conversationId; // null for new conversation
    private String message;

    // Constructors
    public ChatRequest() {
    }

    public ChatRequest(Long conversationId, String message) {
        this.conversationId = conversationId;
        this.message = message;
    }

    // Getters and Setters
    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
