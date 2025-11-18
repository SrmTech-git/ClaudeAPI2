package com.claudechat.dto;

/**
 * Response object containing Claude's reply and token usage information.
 */
public class ChatResponse {

    private Long conversationId;
    private Long messageId;
    private String content;
    private String thinking; // Extended thinking content (if present)
    private TokenUsage tokenUsage;

    // Constructors
    public ChatResponse() {
    }

    // Getters and Setters
    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
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

    public TokenUsage getTokenUsage() {
        return tokenUsage;
    }

    public void setTokenUsage(TokenUsage tokenUsage) {
        this.tokenUsage = tokenUsage;
    }

    /**
     * Inner class to represent token usage for a single message exchange.
     */
    public static class TokenUsage {
        private int inputTokens;
        private int outputTokens;
        private int cacheCreationTokens;
        private int cacheReadTokens;

        public TokenUsage() {
        }

        public TokenUsage(int inputTokens, int outputTokens, int cacheCreationTokens, int cacheReadTokens) {
            this.inputTokens = inputTokens;
            this.outputTokens = outputTokens;
            this.cacheCreationTokens = cacheCreationTokens;
            this.cacheReadTokens = cacheReadTokens;
        }

        public int getInputTokens() {
            return inputTokens;
        }

        public void setInputTokens(int inputTokens) {
            this.inputTokens = inputTokens;
        }

        public int getOutputTokens() {
            return outputTokens;
        }

        public void setOutputTokens(int outputTokens) {
            this.outputTokens = outputTokens;
        }

        public int getCacheCreationTokens() {
            return cacheCreationTokens;
        }

        public void setCacheCreationTokens(int cacheCreationTokens) {
            this.cacheCreationTokens = cacheCreationTokens;
        }

        public int getCacheReadTokens() {
            return cacheReadTokens;
        }

        public void setCacheReadTokens(int cacheReadTokens) {
            this.cacheReadTokens = cacheReadTokens;
        }
    }
}
