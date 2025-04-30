package com.frogcrew.frogcrew.repository;

import com.frogcrew.frogcrew.domain.model.Notification;
import com.frogcrew.frogcrew.domain.model.FrogCrewUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUser_IdAndReadFalse(UUID userId);

    List<Notification> findByUser_Id(UUID userId);

    List<Notification> findByUserOrderByCreatedAtDesc(FrogCrewUser user);
}