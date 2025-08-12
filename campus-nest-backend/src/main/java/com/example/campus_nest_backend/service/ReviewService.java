package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.Requests.ReviewCreateRequestDto;
import com.example.campus_nest_backend.dto.Requests.ReviewUpdateRequestDto;
import com.example.campus_nest_backend.dto.Responses.ReviewResponseDto;
import com.example.campus_nest_backend.dto.Responses.UserSummaryDto;
import com.example.campus_nest_backend.entity.Hostel;
import com.example.campus_nest_backend.entity.Review;
import com.example.campus_nest_backend.entity.User;
import com.example.campus_nest_backend.repository.HostelRepository;
import com.example.campus_nest_backend.repository.ReviewRepository;
import com.example.campus_nest_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final HostelRepository hostelRepository;
    private final UserRepository userRepository;

    public ReviewResponseDto createReview(ReviewCreateRequestDto request) {
        Hostel hostel = validateHostel(request.getHostelId());
        User user = validateUser(request.getUserId());
        validateRating(request.getRating());

        Review review = new Review();
        review.setHostel(hostel);
        review.setUser(user);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        return mapToDto(reviewRepository.save(review));
    }

    public List<ReviewResponseDto> getReviewsForHostel(Long hostelId) {
        validateHostel(hostelId);
        return reviewRepository.findReviewsByHostelId(hostelId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ReviewResponseDto getReviewById(Long reviewId) {
        return mapToDto(validateReview(reviewId));
    }

    public ReviewResponseDto updateReview(Long reviewId, ReviewUpdateRequestDto request) {
        Review review = validateReview(reviewId);

        if (request.getRating() != 0) {
            validateRating(request.getRating());
            review.setRating(request.getRating());
        }
        if (request.getComment() != null && !request.getComment().isBlank()) {
            review.setComment(request.getComment());
        }

        return mapToDto(reviewRepository.save(review));
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.delete(validateReview(reviewId));
    }

    /* ------------------- Validation Helpers ------------------- */

    private Hostel validateHostel(Long hostelId) {
        return hostelRepository.findById(hostelId)
                .orElseThrow(() -> new IllegalArgumentException("Hostel with ID " + hostelId + " not found"));
    }

    private User validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));
    }

    private Review validateReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review with ID " + reviewId + " not found"));
    }

    private void validateRating(Integer rating) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }

    /* ------------------- Mapping ------------------- */
    private ReviewResponseDto mapToDto(Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getComment(),

                review.getRating(),
                review.getCreatedAt(),
                mapToUserSummary(userRepository.findUserById(review.getUser().getId())),
                review.getHostel().getId()

                );
    }
    private UserSummaryDto mapToUserSummary(User user) {
        return new UserSummaryDto(user.getId(), user.getName(), user.getEmail(),
                user.getProfilePicture());
    }

}
