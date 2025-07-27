package com.example.campus_nest_backend.repository;

import com.example.campus_nest_backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
