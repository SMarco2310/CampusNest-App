package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.Requests.ReviewRequest;
import com.example.campus_nest_backend.dto.Responses.ReviewResponse;
import com.example.campus_nest_backend.entity.Hostel;
import com.example.campus_nest_backend.entity.Review;
import com.example.campus_nest_backend.exception.ResourceNotFoundException;
import com.example.campus_nest_backend.repository.HostelRepository;
import com.example.campus_nest_backend.repository.ReviewRepository;
import com.example.campus_nest_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final HostelRepository hostelRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, HostelRepository hostelRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.hostelRepository = hostelRepository;
        this.userRepository = userRepository;
    }

    public ReviewResponse getReviewById(Long id) {
        Review review = reviewRepository.findReviewById(id);
        if (review == null) {
            throw new ResourceNotFoundException("The review with this  Id does not exist");
        }
        return new ReviewResponse(
                review.getId(),
                review.getComment(),
                review.getRating(),
                review.getUser().getId(),
                review.getHostel().getId(),
                review.getUser().getProfilePicture(), // Assuming User entity has a method to get profile image URL
                review.getUser().getName(), // Assuming User entity has a method to get name
                review.getHostel().getName() // Assuming Hostel entity has a method to get name
        );
    }


    public Review addReview(ReviewRequest newReview) {
        Review review = new Review();
        review.setComment(newReview.getComment());
        review.setRating(newReview.getRating());
        review.setHostel(hostelRepository.getHostelById(newReview.getHostel()));
        review.setUser(userRepository.findUserById(newReview.getUser()));
        return reviewRepository.save(review);
    }

    public ReviewResponse updateReview(Long id, ReviewRequest updateReview) {
        Review existingReview = reviewRepository.findReviewById(id);
        existingReview.setComment(updateReview.getComment());
        existingReview.setRating(updateReview.getRating());
        existingReview.setUser(userRepository.findUserById(updateReview.getUser()));
        reviewRepository.save(existingReview);
        return new ReviewResponse(
                existingReview.getId(),
                existingReview.getComment(),
                existingReview.getRating(),
                existingReview.getUser().getId(),
                existingReview.getHostel().getId(),
                existingReview.getUser().getProfilePicture(), // Assuming User entity has a method to get profile image URL
                existingReview.getUser().getName(), // Assuming User entity has a method to get name
                existingReview.getHostel().getName() // Assuming Hostel entity has a method to get name
        ) ;
    }


    public List<ReviewResponse> getReviewsByHostelId(Long hostelId) {
        Hostel hostel = hostelRepository.getHostelById(hostelId);
        return hostel.getReviews().stream().map(
                review -> new ReviewResponse(
                        review.getId(),
                        review.getComment(),
                        review.getRating(),
                        review.getUser().getId(),
                        review.getHostel().getId(),
                        review.getUser().getProfilePicture(), // Assuming User entity has a method to get profile image URL
                        review.getUser().getName(), // Assuming User entity has a method to get name
                        review.getHostel().getName() // Assuming Hostel entity has a method to get name
                )
        ).toList();

    }

    public void deleteReview(Long id) {
        Review review =  reviewRepository.findReviewById(id);
        reviewRepository.delete(review);
    }
}
