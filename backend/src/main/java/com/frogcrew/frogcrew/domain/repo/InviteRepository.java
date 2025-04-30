package com.frogcrew.frogcrew.domain.repo;

import com.frogcrew.frogcrew.domain.model.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InviteRepository extends JpaRepository<Invite, UUID> {
    Optional<Invite> findByToken(String token);

    boolean existsByEmail(String email);

    List<Invite> findByUsedFalseAndExpiresAtAfter(ZonedDateTime now);
}