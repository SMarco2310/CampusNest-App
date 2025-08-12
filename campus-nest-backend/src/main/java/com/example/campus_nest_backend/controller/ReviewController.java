package com.example.campus_nest_backend.controller;

import com.example.campus_nest_backend.dto.Requests.ReviewCreateRequestDto;
import com.example.campus_nest_backend.dto.Requests.ReviewUpdateRequestDto;
import com.example.campus_nest_backend.dto.Responses.ApiResponse;
import com.example.campus_nest_backend.dto.Responses.ReviewResponseDto;
import com.example.campus_nest_backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponseDto>> createReview(@RequestBody ReviewCreateRequestDto request) {
        ReviewResponseDto createdReview = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        true,
                        "Review created successfully",
                        createdReview
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> getReviewById(@PathVariable Long id) {
        ReviewResponseDto review = reviewService.getReviewById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        true,
                        "Review fetched successfully",
                        review
                )
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> updateReview(
            @PathVariable Long id,
            @RequestBody ReviewUpdateRequestDto request
    ) {
        ReviewResponseDto updatedReview = reviewService.updateReview(id, request);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        true,
                        "Review updated successfully",
                        updatedReview
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        true,
                        "Review deleted successfully",
                        null
                )
        );
    }
}
