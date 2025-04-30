// package com.frogcrew.frogcrew.domain.repo;

// import com.frogcrew.frogcrew.domain.model.FrogCrewUser;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import java.util.Optional;
// import java.util.UUID;

// @Repository
// public interface FrogCrewUserRepository extends JpaRepository<FrogCrewUser,
// UUID> {
// Optional<FrogCrewUser> findByEmail(String email);

// boolean existsByEmail(String email);
// }