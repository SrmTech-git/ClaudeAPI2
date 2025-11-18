package com.claudechat.dto;

import com.claudechat.model.Context;

/**
 * Data Transfer Object for Context.
 * Contains the system prompt text.
 */
public class ContextDTO {

    private String systemPrompt;

    // Constructors
    public ContextDTO() {
    }

    public ContextDTO(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    // Getters and Setters
    public String getSystemPrompt() {
        return systemPrompt;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    /**
     * Create DTO from entity.
     */
    public static ContextDTO fromEntity(Context context) {
        if (context == null) {
            return new ContextDTO("");
        }
        return new ContextDTO(context.getSystemPrompt());
    }

    /**
     * Convert to entity.
     */
    public Context toEntity() {
        return new Context(this.systemPrompt != null ? this.systemPrompt : "");
    }
}
