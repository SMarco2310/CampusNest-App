package com.example.campus_nest_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "hostel_id"})
})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Review comment is required")
    @Size(min = 10, max = 500, message = "Comment must be between 10 and 500 characters")
    @Column(nullable = false, name = "comment", length = 500)
    private String comment;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    @NotNull(message = "Rating is required")
    @Column(nullable = false, name = "rating")
    private int rating;

    @NotNull(message = "Student is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;


    @NotNull(message = "Hostel is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_id", nullable = false)
    private Hostel hostel;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private ReviewStatus status = ReviewStatus.APPROVED;

    public enum ReviewStatus {
        PENDING, APPROVED, REJECTED, FLAGGED
    }

    public Review() {
        this.createdAt = LocalDateTime.now();
    }

    public Review(Student student, Hostel hostel, int rating, String comment) {
        this();
        this.student = student;
        this.hostel = hostel;
        this.rating = rating;
        this.comment = comment;
    }

    public boolean isVisible() {
        return status == ReviewStatus.APPROVED;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdAt;
    }
}
