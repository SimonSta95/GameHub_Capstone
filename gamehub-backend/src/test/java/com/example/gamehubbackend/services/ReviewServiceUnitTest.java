package com.example.gamehubbackend.services;

import com.example.gamehubbackend.exceptions.ReviewNotFoundException;
import com.example.gamehubbackend.models.Review;
import com.example.gamehubbackend.models.ReviewDTO;
import com.example.gamehubbackend.repositories.ReviewRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceUnitTest {

    private final ReviewRepository reviewRepository = mock(ReviewRepository.class);
    private final IdService idService = mock(IdService.class);
    private final ReviewService reviewService = new ReviewService(reviewRepository, idService);

    @Test
    void getAllReviews_Test() {
        List<Review> reviews = List.of(
                new Review("1", "user1", "game1", "username1", 4.5, "Great game!", "2020-01-01"),
                new Review("2", "user2", "game2", "username2", 3.5, "Good game", "2020-01-02")
        );

        when(reviewRepository.findAll()).thenReturn(reviews);

        List<Review> actualReviews = reviewService.getAllReviews();
        assertEquals(reviews, actualReviews);
        verify(reviewRepository).findAll();
    }

    @Test
    void getReviewsByGameId_Test() {
        String gameId = "game1";
        List<Review> reviews = List.of(
                new Review("1", "user1", gameId, "username1", 4.5, "Great game!", "2020-01-01")
        );

        when(reviewRepository.findByGameId(gameId)).thenReturn(Optional.of(reviews));

        List<Review> actualReviews = reviewService.getReviewsByGameId(gameId);
        assertEquals(reviews, actualReviews);
        verify(reviewRepository).findByGameId(gameId);
    }

    @Test
    void addReview_Test() {
        ReviewDTO reviewDTO = new ReviewDTO("user1", "game1", "username1", 4.5, "Great game!", "2020-01-01");
        Review reviewToSave = new Review("1", "user1", "game1", "username1", 4.5, "Great game!", "2020-01-01");

        when(idService.randomId()).thenReturn("1");
        when(reviewRepository.save(reviewToSave)).thenReturn(reviewToSave);

        reviewService.addReview(reviewDTO);

        verify(idService).randomId();
        verify(reviewRepository).save(reviewToSave);
    }

    @Test
    void updateReview_Test() {
        String reviewId = "1";
        Review existingReview = new Review(reviewId, "user1", "game1", "username1", 4.0, "Good game", "2020-01-01");
        ReviewDTO updatedReviewDTO = new ReviewDTO("user1", "game1", "username1", 4.5, "Great game!", "2020-01-01");
        Review updatedReview = new Review(reviewId, "user1", "game1", "username1", 4.5, "Great game!", "2020-01-01");

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(updatedReview)).thenReturn(updatedReview);

        reviewService.updateReview(updatedReviewDTO, reviewId);
        verify(reviewRepository).findById(reviewId);
        verify(reviewRepository).save(updatedReview);
    }

    @Test
    void updateReview_ReviewNotFound_Test() {
        String reviewId = "1";
        ReviewDTO updatedReviewDTO = new ReviewDTO("user1", "game1", "username1", 4.5, "Great game!", "2020-01-01");

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.updateReview(updatedReviewDTO, reviewId));
        verify(reviewRepository).findById(reviewId);
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void deleteReview_Test() {
        String reviewId = "1";

        doNothing().when(reviewRepository).deleteById(reviewId);
        reviewService.deleteReview(reviewId);

        verify(reviewRepository).deleteById(reviewId);
    }
}