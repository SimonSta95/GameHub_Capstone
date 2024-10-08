package com.example.gamehubbackend.services;

import com.example.gamehubbackend.exceptions.ReviewNotFoundException;
import com.example.gamehubbackend.models.Review;
import com.example.gamehubbackend.dto.ReviewDTO;
import com.example.gamehubbackend.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final IdService idService;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByGameId(String gameId) {
        return reviewRepository.findByGameId(gameId).orElseThrow(() -> new ReviewNotFoundException("No notes found for id: " + gameId));
    }

    public List<Review> getReviewsByUserId(String userId) {
        return reviewRepository.findByUserId(userId).orElseThrow(() -> new ReviewNotFoundException("No notes found for id: " + userId));
    }

    public void addReview(ReviewDTO review) {

        Review reviewToAdd = new Review(
                idService.randomId(),
                review.gameTitle(),
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

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException("Review with ID " + reviewId + " not found."))
                .withId(reviewId)
                .withGameTitle(updatedReview.gameTitle())
                .withUserId(updatedReview.userId())
                .withGameId(updatedReview.gameId())
                .withUsername(updatedReview.username())
                .withRating(updatedReview.rating())
                .withContent(updatedReview.content())
                .withDate(updatedReview.date());

        reviewRepository.save(review);
    }
}
