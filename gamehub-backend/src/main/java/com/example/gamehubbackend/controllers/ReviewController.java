package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.Review;
import com.example.gamehubbackend.models.ReviewDTO;
import com.example.gamehubbackend.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;  // Service for handling review operations

    /**
     * Retrieve a list of all reviews.
     *
     * @return a list of Review objects
     */
    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    /**
     * Retrieve reviews for a specific game by its ID.
     *
     * @param gameId the ID of the game
     * @return a list of Review objects associated with the specified game
     */
    @GetMapping("/{gameId}")
    public List<Review> getReviewsByGameId(@PathVariable String gameId) {
        return reviewService.getReviewsByGameId(gameId);
    }

    /**
     * Add a new review.
     *
     * @param review the ReviewDTO object containing review details
     */
    @PostMapping
    public void addReview(@RequestBody ReviewDTO review) {
        reviewService.addReview(review);
    }

    /**
     * Update an existing review.
     *
     * @param review the ReviewDTO object containing updated review details
     * @param reviewId the ID of the review to update
     */
    @PutMapping("/{reviewId}")
    public void updateReview(@RequestBody ReviewDTO review, @PathVariable String reviewId) {
        reviewService.updateReview(review, reviewId);
    }

    /**
     * Delete a review by its ID.
     *
     * @param reviewId the ID of the review to delete
     */
    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable String reviewId) {
        reviewService.deleteReview(reviewId);
    }
}
