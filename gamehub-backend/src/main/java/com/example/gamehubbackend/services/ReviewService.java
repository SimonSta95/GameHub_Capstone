package com.example.gamehubbackend.services;

import com.example.gamehubbackend.exceptions.ReviewNotFoundException;
import com.example.gamehubbackend.models.Review;
import com.example.gamehubbackend.dto.ReviewDTO;
import com.example.gamehubbackend.models.UserResponse;
import com.example.gamehubbackend.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final UserService userService;
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

    public void deleteReview(String reviewId) throws AccessDeniedException {

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(reviewId));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User principal = (OAuth2User) authentication.getPrincipal();
            if (!isOwner(review, principal)) {
                throw new AccessDeniedException("Only the owner of the review can delete it.");
            }
        } else {
            String currentUserId = authentication.getName();
            if (!isOwner(review, currentUserId)) {
                throw new AccessDeniedException("Only the owner of the review can delete it.");
            }
        }

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

    private boolean isOwner(Review review, OAuth2User principal) {
        String githubId = principal.getAttribute("id").toString();
        UserResponse user = userService.getUserByGitHubId(githubId);
        if (user != null) {
            String currentUserId = user.id();
            return review.userId().equals(currentUserId);
        } else {
            // Handle the case where the user is not found
            return false;
        }
    }

    private boolean isOwner(Review review, String currentUserId) {

        UserResponse user = userService.getUserByUsername(currentUserId);
        return review.userId().equals(user.id());
    }
}
