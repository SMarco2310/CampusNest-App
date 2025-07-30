package com.example.campus_nest_backend.controller;

import com.example.campus_nest_backend.dto.ReviewRequest;
import com.example.campus_nest_backend.entity.Review;
import com.example.campus_nest_backend.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;


    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @GetMapping("/{hostelId}/reviews")
    public ResponseEntity<List<Review>> getReviewByHostelId(@Valid @PathVariable Long hostelId){
        return ResponseEntity.ok(reviewService.getReviewsByHostelId(hostelId));
    }

    @GetMapping("/review/{id}")
    public ResponseEntity<?> getReviewById(@Valid @PathVariable Long id){
        return ResponseEntity.ok(Map.of("review",reviewService.getReviewById(id)));
    }

    @PostMapping("/review")
    public ResponseEntity<?> addReview(@Valid @RequestBody ReviewRequest reviewRequest){
        return ResponseEntity.ok(Map.of("review",reviewService.addReview(reviewRequest)));
    }

    @PutMapping("/review/{id}")
    public ResponseEntity<?> updateReview(@Valid @PathVariable Long id, @Valid @RequestBody ReviewRequest reviewRequest){
        return ResponseEntity.ok(
                Map.of("review", reviewService.updateReview(id, reviewRequest),
                        "message","Review updated successfully"
        ));
    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity<?> deleteReview(@Valid @PathVariable Long id){
        reviewService.deleteReview(id);
        return ResponseEntity.ok(Map.of("message","Review deleted successfully"));
    }


}
