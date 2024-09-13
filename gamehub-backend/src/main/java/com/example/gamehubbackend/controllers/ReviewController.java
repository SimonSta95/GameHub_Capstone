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

    private final ReviewService reviewService;

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{gameId}")
    public List<Review> getReviewsByGameId(@PathVariable String gameId) {
        return reviewService.getReviewsByGameId(gameId);
    }

    @GetMapping("/user/{userId}")
    public List<Review> getReviewsByUserId(@PathVariable String userId) {
        return reviewService.getReviewsByUserId(userId);
    }

    @PostMapping
    public void addReview(@RequestBody ReviewDTO review) {
        reviewService.addReview(review);
    }

    @PutMapping("/{reviewId}")
    public void updateReview(@RequestBody ReviewDTO review, @PathVariable String reviewId) {
        reviewService.updateReview(review, reviewId);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable String reviewId) {
        reviewService.deleteReview(reviewId);
    }
}
