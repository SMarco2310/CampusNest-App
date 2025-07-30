package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.ReviewRequest;
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

    public Review getReviewById(Long id) {
        Review review = reviewRepository.findReviewById(id);
        if (review == null) {
            throw new ResourceNotFoundException("The review with this  Id does not exist");
        }
        return review;
    }


    public Review addReview(ReviewRequest newReview) {
        Review review = new Review();
        review.setComment(newReview.getComment());
        review.setRating(newReview.getRating());
        review.setHostel(hostelRepository.getHostelById(newReview.getHostel()));
        review.setUser(userRepository.findUserById(newReview.getUser()));
        return reviewRepository.save(review);
    }

    public Review updateReview(Long id, ReviewRequest updateReview) {
        Review existingReview = getReviewById(id);
        existingReview.setComment(updateReview.getComment());
        existingReview.setRating(updateReview.getRating());
        existingReview.setUser(userRepository.findUserById(updateReview.getUser()));
        return reviewRepository.save(existingReview);
    }


    public List<Review> getReviewsByHostelId(Long hostelId) {
        Hostel hostel = hostelRepository.getHostelById(hostelId);
        return hostel.getReviews();
    }

    public void deleteReview(Long id) {
        Review review = getReviewById(id);
        reviewRepository.delete(review);
    }
}
