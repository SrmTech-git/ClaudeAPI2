package com.claudechat.repository;

import com.claudechat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for accessing Message data.
 * Spring Data JPA automatically implements basic CRUD operations.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
