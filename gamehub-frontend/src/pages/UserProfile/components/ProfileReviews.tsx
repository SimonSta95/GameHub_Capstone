import { Box, Card, CardContent, Rating, Typography } from "@mui/material";
import { Review } from "../../../types.ts";

type GameReviewsProps = {
    reviews: Review[];
};

export default function GameReviews({ reviews }: Readonly<GameReviewsProps>) {
    return (
        <Box mt={4}>
            <Typography variant="h5" sx={{ fontWeight: 600, marginBottom: 2 }}>
                Your Reviews
            </Typography>

            {/* Review List */}
            <Box sx={{ marginBottom: 4 }}>
                {reviews.length === 0 ? (
                    <Typography>No reviews yet.</Typography>
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
                            </CardContent>
                        </Card>
                    ))
                )}
            </Box>
        </Box>
    );
}