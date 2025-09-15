package com.basicrud.backend.repositories;

import com.basicrud.backend.domain.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  // Optional, but recommended for clarity
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsById(Long id);

    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
}
