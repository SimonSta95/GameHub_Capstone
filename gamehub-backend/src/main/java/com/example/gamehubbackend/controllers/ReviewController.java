package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.Review;
import com.example.gamehubbackend.dto.ReviewDTO;
import com.example.gamehubbackend.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;  // Service for handling review operations

    /**
     * Get all reviews.
     *
     * @return List of all reviews
     */
    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    /**
     * Get reviews by game ID.
     *
     * @param gameId the ID of the game
     * @return List of reviews for the specified game
     */
    @GetMapping("/{gameId}")
    public List<Review> getReviewsByGameId(@PathVariable String gameId) {
        return reviewService.getReviewsByGameId(gameId);
    }

    /**
     * Get reviews by user ID.
     *
     * @param userId the ID of the user
     * @return List of reviews submitted by the specified user
     */
    @GetMapping("/user/{userId}")
    public List<Review> getReviewsByUserId(@PathVariable String userId) {
        return reviewService.getReviewsByUserId(userId);
    }

    /**
     * Add a new review.
     *
     * @param review the data transfer object containing review details
     */
    @PostMapping
    public void addReview(@RequestBody ReviewDTO review) {
        reviewService.addReview(review);
    }

    /**
     * Update an existing review.
     *
     * @param review the data transfer object containing updated review details
     * @param reviewId the ID of the review to be updated
     */
    @PutMapping("/{reviewId}")
    public void updateReview(@RequestBody ReviewDTO review, @PathVariable String reviewId) {
        reviewService.updateReview(review, reviewId);
    }

    /**
     * Delete a review by its ID.
     *
     * @param reviewId the ID of the review to be deleted
     * @return no content and status 204
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Object> deleteReview(@PathVariable String reviewId) throws AccessDeniedException {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
