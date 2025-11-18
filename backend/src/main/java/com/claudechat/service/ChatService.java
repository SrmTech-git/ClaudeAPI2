package com.claudechat.service;

import com.claudechat.dto.ChatResponse;
import com.claudechat.model.Conversation;
import com.claudechat.model.Context;
import com.claudechat.model.Message;
import com.claudechat.repository.ConversationRepository;
import com.claudechat.repository.ContextRepository;
import com.claudechat.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing chat conversations.
 * Handles creating conversations, sending messages, and storing responses.
 */
@Service
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final ContextRepository contextRepository;
    private final ClaudeApiClient claudeApiClient;

    public ChatService(ConversationRepository conversationRepository,
                      MessageRepository messageRepository,
                      ContextRepository contextRepository,
                      ClaudeApiClient claudeApiClient) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.contextRepository = contextRepository;
        this.claudeApiClient = claudeApiClient;
    }

    /**
     * Send a message to Claude and store the conversation.
     *
     * @param conversationId The ID of an existing conversation, or null for a new one
     * @param userMessage The message from the user
     * @return Response containing Claude's reply and token usage
     */
    @Transactional
    public ChatResponse sendMessage(Long conversationId, String userMessage) {
        // Get or create conversation
        Conversation conversation;
        if (conversationId != null) {
            conversation = conversationRepository.findById(conversationId)
                    .orElseThrow(() -> new RuntimeException("Conversation not found"));
        } else {
            conversation = new Conversation();
            conversation = conversationRepository.save(conversation);
        }

        // Save user's message
        Message userMsg = new Message("user", userMessage);
        conversation.addMessage(userMsg);
        messageRepository.save(userMsg);

        // Build conversation history for Claude API
        List<ClaudeApiClient.ConversationMessage> history = new ArrayList<>();
        for (Message msg : conversation.getMessages()) {
            // Only include the text content, not thinking blocks
            if (msg.getId() != null) { // Skip the just-added message
                history.add(new ClaudeApiClient.ConversationMessage(msg.getRole(), msg.getContent()));
            }
        }

        // Get the system prompt from context
        String baseSystemPrompt = contextRepository.findFirstByOrderByIdAsc()
                .map(Context::getSystemPrompt)
                .orElse("");

        // Add current timestamp to system prompt
        String systemPrompt = buildSystemPromptWithTimestamp(baseSystemPrompt);

        // Call Claude API with system prompt
        ClaudeApiClient.ClaudeResponse claudeResponse = claudeApiClient.sendMessage(history, userMessage, systemPrompt);

        // Save Claude's response
        Message assistantMsg = new Message("assistant", claudeResponse.content);
        assistantMsg.setThinking(claudeResponse.thinking);
        assistantMsg.setInputTokens(claudeResponse.inputTokens);
        assistantMsg.setOutputTokens(claudeResponse.outputTokens);
        assistantMsg.setCacheCreationTokens(claudeResponse.cacheCreationInputTokens);
        assistantMsg.setCacheReadTokens(claudeResponse.cacheReadInputTokens);
        conversation.addMessage(assistantMsg);

        // Update conversation token totals
        conversation.addTokenUsage(
                claudeResponse.inputTokens,
                claudeResponse.outputTokens,
                claudeResponse.cacheCreationInputTokens,
                claudeResponse.cacheReadInputTokens
        );

        messageRepository.save(assistantMsg);
        conversationRepository.save(conversation);

        // Build response
        ChatResponse response = new ChatResponse();
        response.setConversationId(conversation.getId());
        response.setMessageId(assistantMsg.getId());
        response.setContent(claudeResponse.content);
        response.setThinking(claudeResponse.thinking);

        ChatResponse.TokenUsage tokenUsage = new ChatResponse.TokenUsage(
                claudeResponse.inputTokens,
                claudeResponse.outputTokens,
                claudeResponse.cacheCreationInputTokens,
                claudeResponse.cacheReadInputTokens
        );
        response.setTokenUsage(tokenUsage);

        return response;
    }

    /**
     * Get all conversations ordered by most recent first.
     */
    public List<Conversation> getAllConversations() {
        return conversationRepository.findAllByOrderByUpdatedAtDesc();
    }

    /**
     * Get a specific conversation with all messages.
     */
    public Conversation getConversation(Long conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
    }

    /**
     * Delete a conversation and all its messages.
     */
    @Transactional
    public void deleteConversation(Long conversationId) {
        conversationRepository.deleteById(conversationId);
    }

    /**
     * Build the complete system prompt by appending the current timestamp.
     *
     * @param basePrompt The base system prompt from context (can be empty)
     * @return The complete system prompt with timestamp
     */
    private String buildSystemPromptWithTimestamp(String basePrompt) {
        // Format current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm:ss a");
        String timestamp = now.format(formatter);

        // Build the timestamp message
        String timestampInfo = "\n\nCurrent local date and time: " + timestamp;

        // Append timestamp to base prompt
        if (basePrompt != null && !basePrompt.trim().isEmpty()) {
            return basePrompt.trim() + timestampInfo;
        } else {
            // If no base prompt, just return the timestamp info (without leading newlines)
            return "Current local date and time: " + timestamp;
        }
    }
}
