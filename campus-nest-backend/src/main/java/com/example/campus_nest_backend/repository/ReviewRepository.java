package com.example.campus_nest_backend.repository;

import com.example.campus_nest_backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findReviewById(Long id);

    List<Review> findReviewsByHostelId(Long hostelId);
}
