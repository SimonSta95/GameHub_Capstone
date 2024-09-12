import {GameDetailAPIResponse, User} from "../../types.ts";
import {useNavigate, useParams} from "react-router-dom";
import {Box, Button, Card, CardContent, Chip, CircularProgress, Typography} from "@mui/material";
import {useEffect, useState} from "react";
import axios from "axios";
import GameNotes from "./components/GameNotes.tsx";
import GameReviews from "./components/GameReviews.tsx";

type GameDetailProps = {
    user: User | null;
};

export default function GameDetail(props: Readonly<GameDetailProps>) {
    const [game, setGame] = useState<GameDetailAPIResponse | undefined | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();
    const params = useParams();
    const id: string | undefined = params.id;

    const fetchGame = async () => {
        try {
            const response = await axios.get(`/api/games/fetch/${id}`);
            setGame(response.data);
            setLoading(false);
        } catch (error) {
            console.error(error);
            setError("Failed to fetch game details.");
            setLoading(false);
        }
    };

    useEffect(() => {
        if (id) {
            fetchGame();
        }
    }, [id]);

    if (loading) {
        return (
            <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", height: "100vh" }}>
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Box sx={{ textAlign: "center", padding: "24px" }}>
                <Typography variant="h6" color="error">
                    {error}
                </Typography>
            </Box>
        );
    }

    if (!game) {
        return (
            <Box sx={{ textAlign: "center", padding: "24px" }}>
                <Typography variant="h6">No game details found.</Typography>
            </Box>
        );
    }

    return (
        <Box sx={{ maxWidth: "1200px", margin: "0 auto", padding: "32px 16px" }}>
            {/* Back to Gallery Button */}
            <Button
                onClick={() => navigate("/games")}
                variant="outlined"
                color="primary"
                sx={{ marginBottom: "24px" }}
            >
                Back to Gallery
            </Button>
            {/* Hero Section with Game Cover */}
            <Box
                sx={{
                    position: "relative",
                    backgroundImage: `url(${game.background_image})`,
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                    height: "400px",
                    borderRadius: "12px",
                    boxShadow: "0 8px 30px rgba(0, 0, 0, 0.2)",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    color: "white",
                    overflow: "hidden",
                }}
            >
                <Box
                    sx={{
                        backgroundColor: "rgba(0,0,0,0.6)",
                        width: "100%",
                        height: "100%",
                        padding: 4,
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                    }}
                >
                    <Typography variant="h2" sx={{ fontWeight: 700 }}>
                        {game.name}
                    </Typography>
                </Box>
            </Box>

            {/* Game Info Section */}
            <Box
                sx={{
                    display: "flex",
                    flexDirection: "row",
                    gap: "32px",
                    marginTop: "32px",
                    justifyContent: "space-between",
                    alignItems: "stretch",
                    flexWrap: "wrap",
                }}
            >
                {/* Left Column: Game Description & Platforms */}
                <Card
                    sx={{
                        flex: "1",
                        borderRadius: "12px",
                        boxShadow: "0 4px 20px rgba(0, 0, 0, 0.1)",
                        display: "flex",
                        flexDirection: "column",
                    }}
                >
                    <CardContent sx={{ flexGrow: 1 }}>
                        <Typography
                            variant="h5"
                            sx={{
                                fontWeight: 600,
                                marginBottom: 2,
                                color: "primary.main",
                            }}
                        >
                            About the Game
                        </Typography>
                        <Typography
                            variant="body1"
                            sx={{ lineHeight: 1.6, color: "#555" }}
                            dangerouslySetInnerHTML={{ __html: game.description }}
                        />

                        <Typography
                            variant="h5"
                            sx={{
                                fontWeight: 600,
                                marginBottom: 2,
                                color: "primary.main",
                            }}
                        >
                            Platforms
                        </Typography>
                        <Box sx={{ display: "flex", gap: 1, marginTop: 2, flexWrap: "wrap" }}>
                            {game.platforms.map((platformData) => (
                                <Chip
                                    key={platformData.platform.name}
                                    label={platformData.platform.name}
                                    variant="outlined"
                                    sx={{
                                        textTransform: "capitalize",
                                        fontWeight: 500,
                                        fontSize: "0.85rem",
                                        borderRadius: "12px",
                                        borderColor: "primary.light",
                                        color: "primary.main",
                                    }}
                                />
                            ))}
                        </Box>
                    </CardContent>
                </Card>

                {/* Right Column: Additional Info */}
                <Card
                    sx={{
                        flex: "1",
                        maxWidth: "350px",
                        borderRadius: "12px",
                        boxShadow: "0 4px 20px rgba(0, 0, 0, 0.1)",
                        display: "flex",
                        flexDirection: "column",
                    }}
                >
                    <CardContent>
                        <Typography variant="h6" sx={{ fontWeight: 600, marginBottom: 2 }}>
                            Game Details
                        </Typography>
                        <Typography variant="body2" sx={{ color: "#555", marginBottom: 1 }}>
                            <strong>Release Date:</strong> {game.released}
                        </Typography>
                        <Typography variant="body2" sx={{ color: "#555", marginBottom: 1 }}>
                            <strong>Developer:</strong> {game.developers.map((dev) => dev.name).join(", ")}
                        </Typography>
                        <Typography variant="body2" sx={{ color: "#555", marginBottom: 1 }}>
                            <strong>Publisher:</strong> {game.publishers.map((pub) => pub.name).join(", ")}
                        </Typography>
                        <Typography variant="body2" sx={{ color: "#555", marginBottom: 1 }}>
                            <strong>Genre:</strong> {game.genres.map((genre) => genre.name).join(", ")}
                        </Typography>
                    </CardContent>
                </Card>
            </Box>
            <GameNotes game={game} user={props.user} />
            <GameReviews gameId={game.id.toString()} user={props.user} />
        </Box>
    );
}