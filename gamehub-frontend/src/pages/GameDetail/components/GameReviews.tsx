import { useState, useEffect } from 'react';
import { Box, Button, Card, CardContent, CircularProgress, Rating, TextField, Typography, Dialog, DialogActions, DialogContent, DialogTitle, Snackbar, Alert } from "@mui/material";
import axios from "axios";
import { User, Review } from "../../../types.ts";

type GameReviewsProps = {
    gameId: string;
    user: User | null;
};

export default function GameReviews({ gameId, user }: Readonly<GameReviewsProps>) {
    const [reviews, setReviews] = useState<Review[]>([]);
    const [newReview, setNewReview] = useState<string>("");
    const [newRating, setNewRating] = useState<number | null>(null);
    const [averageRating, setAverageRating] = useState<number | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [editReviewId, setEditReviewId] = useState<string | null>(null);
    const [editReviewContent, setEditReviewContent] = useState<string>("");
    const [editReviewRating, setEditReviewRating] = useState<number | null>(null);
    const [confirmDeleteDialogOpen, setConfirmDeleteDialogOpen] = useState(false);
    const [reviewToDelete, setReviewToDelete] = useState<string | null>(null);
    const [submitting, setSubmitting] = useState(false);
    const [deleting, setDeleting] = useState(false);
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState<string>("");
    const [userReview, setUserReview] = useState<Review | null>(null);

    // Fetch reviews and user review for the game
    const fetchReviews = async () => {
        try {
            const response = await axios.get(`/api/reviews/${gameId}`);
            const fetchedReviews = response.data;
            setReviews(fetchedReviews);

            // Calculate average rating
            const totalRating = fetchedReviews.reduce((sum: number, review: Review) => sum + review.rating, 0);
            const average = fetchedReviews.length > 0 ? totalRating / fetchedReviews.length : 0;
            setAverageRating(average);

            // Find the user's review if it exists
            const userReview = fetchedReviews.find((review: Review) => review.userId === user?.id) || null;
            setUserReview(userReview);

            setLoading(false);
        } catch (error) {
            console.log(error)
            setError("Failed to fetch reviews.");
            setLoading(false);
        }
    };

    // Add or update a review
    const handleSubmitReview = async () => {
        if (!user) {
            setError("You must be logged in to add a review.");
            return;
        }
        if (editReviewId && editReviewRating === null) {
            setError("Please select a rating.");
            return;
        }
        if (!editReviewId && newRating === null) {
            setError("Please select a rating.");
            return;
        }
        if (userReview && !editReviewId) {
            setError("You have already submitted a review for this game.");
            return;
        }
        setSubmitting(true);
        try {
            if (editReviewId) {
                // Update existing review
                await axios.put(`/api/reviews/${editReviewId}`, {
                    userId: user.id,
                    gameId: gameId,
                    username: user.username,
                    rating: editReviewRating,
                    content: editReviewContent,
                    date: new Date().toISOString().split("T")[0]
                });
                setEditReviewId(null); // Reset edit mode
                setSnackbarMessage("Review updated successfully!");
            } else {
                // Add new review
                await axios.post(`/api/reviews`, {
                    userId: user.id,
                    gameId: gameId,
                    username: user.username,
                    rating: newRating,
                    content: newReview,
                    date: new Date().toISOString().split("T")[0]
                });
                setNewReview("");
                setNewRating(null); // Reset rating after submit
                setSnackbarMessage("Review added successfully!");
            }
            fetchReviews(); // Refresh reviews
        } catch (error) {
            console.log(error)
            setError("Failed to submit review.");
        } finally {
            setSubmitting(false);
            setSnackbarOpen(true);
        }
    };

    // Handle edit review
    const handleEdit = (review: Review) => {
        setEditReviewId(review.id);
        setEditReviewContent(review.content);
        setEditReviewRating(review.rating);
    };

    // Cancel edit review
    const handleCancelEdit = () => {
        setEditReviewId(null);
        setEditReviewContent("");
        setEditReviewRating(null);
    };

    // Handle delete review
    const handleDelete = (reviewId: string) => {
        setReviewToDelete(reviewId);
        setConfirmDeleteDialogOpen(true);
    };

    // Confirm delete review
    const confirmDelete = async () => {
        if (reviewToDelete) {
            setDeleting(true);
            try {
                await axios.delete(`/api/reviews/${reviewToDelete}`);
                fetchReviews(); // Refresh reviews after deletion
                setConfirmDeleteDialogOpen(false);
                setReviewToDelete(null);
                setSnackbarMessage("Review deleted successfully!");
            } catch (error) {
                console.log(error)
                setError("Failed to delete review.");
            } finally {
                setDeleting(false);
                setSnackbarOpen(true);
            }
        }
    };

    // Cancel delete review
    const cancelDelete = () => {
        setConfirmDeleteDialogOpen(false);
        setReviewToDelete(null);
    };

    useEffect(() => {
        fetchReviews();
    }, [gameId]);

    if (loading) {
        return <CircularProgress />;
    }

    return (
        <Box mt={4}>
            <Typography variant="h5" sx={{ fontWeight: 600, marginBottom: 2 }}>
                Reviews
            </Typography>

            {error && (
                <Typography color="error" sx={{ marginBottom: 2 }}>
                    {error}
                </Typography>
            )}

            {/* Average Rating */}
            {averageRating !== null && (
                <Box sx={{ marginBottom: 4 }}>
                    <Typography variant="h6" sx={{ fontWeight: 600 }}>
                        Average Rating
                    </Typography>
                    <Rating value={averageRating} readOnly precision={0.1} sx={{ marginBottom: 2 }} />
                    <Typography variant="body1" sx={{ color: "#555" }}>
                        {averageRating.toFixed(1)} out of 5
                    </Typography>
                </Box>
            )}

            {/* Review List */}
            <Box sx={{ marginBottom: 4 }}>
                {reviews.length === 0 ? (
                    <Typography>No reviews yet. Be the first to add one!</Typography>
                ) : (
                    reviews.map((review) => (
                        <Card key={review.id} sx={{ marginBottom: 2 }}>
                            <CardContent>
                                <Typography variant="body1">
                                    <strong>{review.username}</strong> on {review.date}:
                                </Typography>
                                <Rating value={review.rating} readOnly precision={0.5} sx={{ marginBottom: 1 }} />
                                <Typography variant="body2" sx={{ color: "#555" }}>
                                    {review.content}
                                </Typography>
                                {user && user.id === review.userId && (
                                    <Box sx={{ marginTop: 2 }}>
                                        <Button variant="outlined" color="primary" onClick={() => handleEdit(review)}>
                                            Edit
                                        </Button>
                                        <Button variant="outlined" color="error" onClick={() => handleDelete(review.id)} sx={{ marginLeft: 2 }}>
                                            Delete
                                        </Button>
                                    </Box>
                                )}
                            </CardContent>
                        </Card>
                    ))
                )}
            </Box>

            {/* Add or Edit Review Form */}
            {user ? (
                <Box>
                    <Typography variant="h6" sx={{ marginBottom: 2 }}>
                        {editReviewId ? "Edit Your Review" : "Add Your Review"}
                    </Typography>
                    <Rating
                        value={editReviewId ? editReviewRating : newRating}
                        onChange={(_event, newValue) => editReviewId ? setEditReviewRating(newValue) : setNewRating(newValue)}
                        precision={0.5}
                        sx={{ marginBottom: 2 }}
                    />
                    <TextField
                        fullWidth
                        label="Write your review"
                        multiline
                        rows={4}
                        value={editReviewId ? editReviewContent : newReview}
                        onChange={(e) => editReviewId ? setEditReviewContent(e.target.value) : setNewReview(e.target.value)}
                        sx={{ marginBottom: 2 }}
                    />
                    <Button variant="contained" color="primary" onClick={handleSubmitReview} disabled={submitting}>
                        {editReviewId ? "Update Review" : "Submit Review"}
                    </Button>
                    {editReviewId && (
                        <Button variant="outlined" color="secondary" onClick={handleCancelEdit} sx={{ marginLeft: 2 }}>
                            Cancel
                        </Button>
                    )}
                </Box>
            ) : (
                <Typography>You must be logged in to add or edit a review.</Typography>
            )}

            {/* Confirmation Dialog */}
            <Dialog
                open={confirmDeleteDialogOpen}
                onClose={cancelDelete}
                aria-labelledby="confirm-delete-dialog-title"
                aria-describedby="confirm-delete-dialog-description"
            >
                <DialogTitle id="confirm-delete-dialog-title">Confirm Delete</DialogTitle>
                <DialogContent>
                    <Typography id="confirm-delete-dialog-description">
                        Are you sure you want to delete this review? This action cannot be undone.
                    </Typography>
                </DialogContent>
                <DialogActions>
                    <Button onClick={cancelDelete} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={confirmDelete} color="error" disabled={deleting}>
                        Delete
                    </Button>
                </DialogActions>
            </Dialog>

            {/* Snackbar for feedback */}
            <Snackbar
                open={snackbarOpen}
                autoHideDuration={6000}
                onClose={() => setSnackbarOpen(false)}
            >
                <Alert onClose={() => setSnackbarOpen(false)} severity="success">
                    {snackbarMessage}
                </Alert>
            </Snackbar>
        </Box>
    );
}