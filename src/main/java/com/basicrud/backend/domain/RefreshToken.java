package com.basicrud.backend.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
 

@Entity
public class RefreshToken {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Created At cannot be null")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "Expires At cannot be null")
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    public RefreshToken() {
    }

    public RefreshToken(User user, LocalDateTime createdAt) {
        this.user = user;
        this.createdAt = createdAt;
        this.expiresAt = createdAt.plusDays(7); // Default expiration of 7 days
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID uuid) {
        this.id = uuid;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        if (createdAt.isAfter(LocalDateTime.now( ))) {
            throw new IllegalArgumentException("Created At cannot be in the future");
        }

        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        if (expiresAt.isBefore(createdAt)) {
            throw new IllegalArgumentException("Expires At must be after Created At");
        }

        this.expiresAt = expiresAt;
    }
}
