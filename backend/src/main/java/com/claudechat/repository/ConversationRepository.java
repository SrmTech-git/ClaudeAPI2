package com.claudechat.repository;

import com.claudechat.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for accessing Conversation data.
 * Spring Data JPA automatically implements basic CRUD operations.
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    /**
     * Find all conversations ordered by most recently updated first.
     */
    List<Conversation> findAllByOrderByUpdatedAtDesc();
}
