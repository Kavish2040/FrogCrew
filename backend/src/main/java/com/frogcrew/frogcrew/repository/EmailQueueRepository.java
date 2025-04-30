package com.frogcrew.frogcrew.repository;

import com.frogcrew.frogcrew.model.EmailQueue;
import com.frogcrew.frogcrew.model.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EmailQueueRepository extends JpaRepository<EmailQueue, UUID> {
    List<EmailQueue> findByStatus(EmailStatus status);

    List<EmailQueue> findByStatusAndRetryCountLessThan(EmailStatus status, int maxRetries);

    List<EmailQueue> findBySentAtIsNullOrderByCreatedAtAsc();
}