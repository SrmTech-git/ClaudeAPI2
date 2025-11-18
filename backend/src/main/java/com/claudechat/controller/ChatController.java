package com.claudechat.controller;

import com.claudechat.dto.ChatRequest;
import com.claudechat.dto.ChatResponse;
import com.claudechat.dto.ConversationDTO;
import com.claudechat.dto.ContextDTO;
import com.claudechat.model.Conversation;
import com.claudechat.model.Context;
import com.claudechat.service.ChatService;
import com.claudechat.repository.ContextRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for chat operations.
 * Provides endpoints for sending messages and managing conversations.
 */
@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;
    private final ContextRepository contextRepository;

    public ChatController(ChatService chatService, ContextRepository contextRepository) {
        this.chatService = chatService;
        this.contextRepository = contextRepository;
    }

    /**
     * Send a message to Claude.
     * POST /api/chat
     *
     * @param request Contains the message and optional conversation ID
     * @return Claude's response with thinking and token usage
     */
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> sendMessage(@RequestBody ChatRequest request) {
        try {
            ChatResponse response = chatService.sendMessage(
                    request.getConversationId(),
                    request.getMessage()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log error and return 500
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get all conversations.
     * GET /api/conversations
     *
     * @return List of all conversations with messages
     */
    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationDTO>> getAllConversations() {
        try {
            List<Conversation> conversations = chatService.getAllConversations();
            List<ConversationDTO> dtos = conversations.stream()
                    .map(ConversationDTO::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get a specific conversation.
     * GET /api/conversations/{id}
     *
     * @param id The conversation ID
     * @return The conversation with all messages
     */
    @GetMapping("/conversations/{id}")
    public ResponseEntity<ConversationDTO> getConversation(@PathVariable Long id) {
        try {
            Conversation conversation = chatService.getConversation(id);
            ConversationDTO dto = ConversationDTO.fromEntity(conversation);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Delete a conversation.
     * DELETE /api/conversations/{id}
     *
     * @param id The conversation ID
     * @return 204 No Content on success
     */
    @DeleteMapping("/conversations/{id}")
    public ResponseEntity<Void> deleteConversation(@PathVariable Long id) {
        try {
            chatService.deleteConversation(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get the current context/system prompt.
     * GET /api/context
     *
     * @return The current context
     */
    @GetMapping("/context")
    public ResponseEntity<ContextDTO> getContext() {
        try {
            Context context = contextRepository.findFirstByOrderByIdAsc()
                    .orElse(new Context(""));
            return ResponseEntity.ok(ContextDTO.fromEntity(context));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Update the context/system prompt.
     * PUT /api/context
     *
     * @param contextDTO The new context
     * @return The updated context
     */
    @PutMapping("/context")
    public ResponseEntity<ContextDTO> updateContext(@RequestBody ContextDTO contextDTO) {
        try {
            Context context = contextRepository.findFirstByOrderByIdAsc()
                    .orElse(new Context());
            context.setSystemPrompt(contextDTO.getSystemPrompt());
            context = contextRepository.save(context);
            return ResponseEntity.ok(ContextDTO.fromEntity(context));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
