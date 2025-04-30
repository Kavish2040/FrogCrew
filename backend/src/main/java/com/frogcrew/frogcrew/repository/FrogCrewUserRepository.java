package com.frogcrew.frogcrew.repository;

import com.frogcrew.frogcrew.domain.model.FrogCrewUser;
import com.frogcrew.frogcrew.domain.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FrogCrewUserRepository extends JpaRepository<FrogCrewUser, UUID> {
    @Query("SELECT u FROM FrogCrewUser u WHERE :positionCode MEMBER OF u.positions")
    List<FrogCrewUser> findByPositionsContaining(@Param("positionCode") String positionCode);

    Optional<FrogCrewUser> findByEmail(String email);

    Optional<FrogCrewUser> findByResetToken(String resetToken);
}