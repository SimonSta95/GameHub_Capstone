package com.example.gamehubbackend.services;

import com.example.gamehubbackend.exceptions.ReviewNotFoundException;
import com.example.gamehubbackend.models.Review;
import com.example.gamehubbackend.models.ReviewDTO;
import com.example.gamehubbackend.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final IdService idService;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByGameId(String gameId) {
        return reviewRepository.findByGameId(gameId);
    }

    public void addReview(ReviewDTO review) {
        Review reviewToAdd = new Review(
                idService.randomId(),
                review.userId(),
                review.gameId(),
                review.username(),
                review.rating(),
                review.content(),
                review.date()
        );
        reviewRepository.save(reviewToAdd);
    }

    public void deleteReview(String reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public void updateReview(ReviewDTO updatedReview, String reviewId) {
        Optional<Review> existingReview = Optional.ofNullable(reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException("Review with ID " + reviewId + " not found.")));
        if (existingReview.isPresent()) {
            Review review = new Review(
                    reviewId,
                    updatedReview.userId(),
                    updatedReview.gameId(),
                    updatedReview.username(),
                    updatedReview.rating(),
                    updatedReview.content(),
                    updatedReview.date()
            );
            reviewRepository.save(review);
        } else {
            throw new ReviewNotFoundException("Review with ID " + reviewId + " not found.");
        }
    }
}
