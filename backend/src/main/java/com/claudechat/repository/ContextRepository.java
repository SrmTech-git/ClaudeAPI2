package com.claudechat.repository;

import com.claudechat.model.Context;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for accessing Context data.
 * Spring Data JPA automatically implements basic CRUD operations.
 */
@Repository
public interface ContextRepository extends JpaRepository<Context, Long> {

    /**
     * Find the first (and only) context record.
     * Since this is a singleton entity, we only store one context.
     */
    Optional<Context> findFirstByOrderByIdAsc();
}
