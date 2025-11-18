package com.claudechat.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for communicating with the Claude API.
 * Handles sending messages and receiving responses with extended thinking.
 */
@Service
public class ClaudeApiClient {

    private final WebClient webClient;
    private final String model;
    private final int maxTokens;

    public ClaudeApiClient(
            @Value("${anthropic.api.key}") String apiKey,
            @Value("${anthropic.api.url}") String apiUrl,
            @Value("${anthropic.api.model}") String model,
            @Value("${anthropic.api.max-tokens}") int maxTokens) {

        this.model = model;
        this.maxTokens = maxTokens;

        // Create WebClient for API calls
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("x-api-key", apiKey)
                .defaultHeader("anthropic-version", "2023-06-01")
                .build();
    }

    /**
     * Send a message to Claude and get a response.
     *
     * @param conversationHistory List of previous messages for context
     * @param userMessage The new message from the user
     * @return Claude's response including thinking blocks and token usage
     */
    public ClaudeResponse sendMessage(List<ConversationMessage> conversationHistory, String userMessage) {
        // Build the messages array
        List<Map<String, Object>> messages = new ArrayList<>();

        // Add conversation history
        for (ConversationMessage msg : conversationHistory) {
            messages.add(createMessageMap(msg.role, msg.content));
        }

        // Add the new user message
        messages.add(createMessageMap("user", userMessage));

        // Build the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("messages", messages);

        // Enable extended thinking
        Map<String, Object> thinkingConfig = new HashMap<>();
        thinkingConfig.put("type", "enabled");
        thinkingConfig.put("budget_tokens", 10000); // Allow up to 10k tokens for thinking
        requestBody.put("thinking", thinkingConfig);

        // Make the API call
        ClaudeApiResponse response = webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(ClaudeApiResponse.class)
                .block(); // Block to wait for response (synchronous call)

        // Parse the response
        return parseResponse(response);
    }

    /**
     * Helper method to create a message map.
     */
    private Map<String, Object> createMessageMap(String role, String content) {
        Map<String, Object> message = new HashMap<>();
        message.put("role", role);
        message.put("content", content);
        return message;
    }

    /**
     * Parse the Claude API response to extract content, thinking, and token usage.
     */
    private ClaudeResponse parseResponse(ClaudeApiResponse apiResponse) {
        ClaudeResponse response = new ClaudeResponse();

        StringBuilder contentBuilder = new StringBuilder();
        StringBuilder thinkingBuilder = new StringBuilder();

        // Process content blocks
        if (apiResponse.content != null) {
            for (ContentBlock block : apiResponse.content) {
                if ("thinking".equals(block.type)) {
                    thinkingBuilder.append(block.thinking);
                } else if ("text".equals(block.type)) {
                    contentBuilder.append(block.text);
                }
            }
        }

        response.content = contentBuilder.toString();
        response.thinking = thinkingBuilder.length() > 0 ? thinkingBuilder.toString() : null;

        // Extract token usage
        if (apiResponse.usage != null) {
            response.inputTokens = apiResponse.usage.inputTokens;
            response.outputTokens = apiResponse.usage.outputTokens;
            response.cacheCreationInputTokens = apiResponse.usage.cacheCreationInputTokens != null
                    ? apiResponse.usage.cacheCreationInputTokens : 0;
            response.cacheReadInputTokens = apiResponse.usage.cacheReadInputTokens != null
                    ? apiResponse.usage.cacheReadInputTokens : 0;
        }

        return response;
    }

    /**
     * Simple class to represent a message in the conversation history.
     */
    public static class ConversationMessage {
        public String role;
        public String content;

        public ConversationMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    /**
     * Response object from Claude API.
     */
    public static class ClaudeResponse {
        public String content;
        public String thinking;
        public int inputTokens;
        public int outputTokens;
        public int cacheCreationInputTokens;
        public int cacheReadInputTokens;
    }

    // POJOs for deserializing Claude API responses
    private static class ClaudeApiResponse {
        public String id;
        public String type;
        public String role;
        public List<ContentBlock> content;
        public String model;

        @JsonProperty("stop_reason")
        public String stopReason;

        @JsonProperty("stop_sequence")
        public String stopSequence;

        public Usage usage;
    }

    private static class ContentBlock {
        public String type;
        public String text;
        public String thinking;
    }

    private static class Usage {
        @JsonProperty("input_tokens")
        public Integer inputTokens;

        @JsonProperty("output_tokens")
        public Integer outputTokens;

        @JsonProperty("cache_creation_input_tokens")
        public Integer cacheCreationInputTokens;

        @JsonProperty("cache_read_input_tokens")
        public Integer cacheReadInputTokens;
    }
}
