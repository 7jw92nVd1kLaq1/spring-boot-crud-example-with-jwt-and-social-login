package com.basicrud.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Represents a comment entity.
 * This class is mapped to the 'comments' table in the database.
 */
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Comment content cannot be blank")
    @Size(min = 1, max = 500, message = "Comment must be between 1 and 500 characters")
    @Column(nullable = false, length = 500)
    private String content;

    /**
     * Establishes a Many-to-One relationship with the User entity.
     * This links a single comment to the user who wrote it.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Establishes a Many-to-One relationship with the Post entity.
     * This links a single comment to the post it belongs to.
     */
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * JPA requires a default, no-argument constructor.
     */
    public Comment() {
    }

    /**
     * Convenience constructor for creating a new Comment instance.
     *
     * @param content The content of the comment.
     * @param user    The user who wrote the comment.
     * @param post    The post the comment belongs to.
     */
    public Comment(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
    }

    /**
     * This method is a JPA lifecycle callback that is called
     * before the entity is first persisted. It sets the initial creation
     * and update timestamps to UTC.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(ZoneOffset.UTC);
        this.updatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    /**
     * This method is a JPA lifecycle callback that is called
     * before an existing entity is updated. It updates the 'updatedAt'
     * timestamp to UTC.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
