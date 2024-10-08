import { Box, Typography, Avatar, Divider, Chip, Card, CardContent } from "@mui/material";
import { Note, Review, User } from "../../types";
import { Link, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import ProfileNotes from "./components/ProfileNotes.tsx";
import GameReviews from "./components/ProfileReviews.tsx";
import { useToaster } from "../../ToasterContext.tsx";

export default function UserProfile() {
    const [user, setUser] = useState<User | null>(null);
    const [notes, setNotes] = useState<Note[]>([]);
    const [reviews, setReviews] = useState<Review[]>([]);

    // Get the user ID from URL parameters
    const params = useParams();
    const { show } = useToaster();

    // Fetch user data from the API
    const fetchUser = () => {
        axios.get(`/api/users/${params.id}`)
            .then(response => {
                console.log(response.data)
                setUser(response.data)})
            .catch(() => {
                show("Failed to fetch User", "error");
                setUser(null);
            });
    };

    // Fetch notes related to the user
    const fetchNotes = () => {
        axios.get(`/api/notes/user/${params.id}`)
            .then(response => setNotes(response.data))
            .catch(() => {
                show("Failed to fetch Notes", "error");
                setNotes([]);
            });
    };

    // Fetch reviews written by the user
    const fetchReviews = () => {
        axios.get(`/api/reviews/user/${params.id}`)
            .then(response => setReviews(response.data))
            .catch(() => {
                show("Failed to fetch Reviews", "error");
                setReviews([]);
            });
    };

    // Fetch data when the component mounts or the user ID changes
    useEffect(() => {
        fetchUser();
        fetchNotes();
        fetchReviews();
    }, [params.id]);

    // Display a message if the user is not found
    if (!user) {
        return (
            <Box sx={{ textAlign: 'center', padding: '24px' }}>
                <Typography variant="h6">User not found.</Typography>
            </Box>
        );
    }

    // Define colors for different platforms
    const platformColors: Record<string, string> = {
        'PC': '#0078d4',
        'PlayStation 3': '#003b5c',
        'PlayStation 4': '#003b5c',
        'PlayStation 5': '#0072ce',
        'Xbox 360': '#4d5d53',
        'Xbox One': '#5b5b5b',
        'Xbox Series X/S': '#0e6f00',
        'Nintendo Switch': '#e4002b',
        'Wii U': '#005b96',
        'Switch': '#e4002b',
        'Stadia': '#e50050',
    };

    return (
        <Box sx={{ maxWidth: '1200px', margin: '0 auto', padding: '32px 16px' }}>
            {/* User Header Section */}
            <Box
                sx={{
                    display: 'flex',
                    alignItems: 'center',
                    background: '#1b263b',
                    padding: '24px',
                    borderRadius: '12px',
                    color: 'white',
                    boxShadow: '0px 4px 15px rgba(0, 0, 0, 0.1)',
                    marginBottom: '32px',
                    position: 'relative',
                    overflow: 'hidden',
                    transition: 'box-shadow 0.3s ease',
                    "&:hover": {
                        boxShadow: '0px 8px 25px rgba(0, 0, 0, 0.2)',
                    },
                }}
            >
                <Avatar
                    src={user.avatarUrl}
                    alt={user.username}
                    sx={{
                        width: 120,
                        height: 120,
                        marginRight: '24px',
                        border: '4px solid #ffffff',
                        transition: 'transform 0.3s ease',
                        "&:hover": {
                            transform: 'scale(1.1)',
                        },
                    }}
                />
                <Box>
                    <Typography variant="h4" sx={{ fontWeight: 700, marginBottom: '8px' }}>
                        {user.username}
                    </Typography>
                </Box>
            </Box>

            {/* Game Library Section */}
            <Box sx={{ marginBottom: '24px' }}>
                <Typography variant="h5" sx={{ fontWeight: 600, marginBottom: '16px', color: '#1b263b' }}>
                    Game Library
                </Typography>
                <Divider sx={{ marginBottom: '16px', borderColor: '#1b263b' }} />

                {user.gameLibrary.length === 0 ? (
                    <Typography variant="body1">No games in the library.</Typography>
                ) : (
                    <Box
                        sx={{
                            display: 'flex',
                            flexWrap: 'wrap',
                            gap: 3,
                            justifyContent: 'center',
                        }}
                    >
                        {user.gameLibrary.map((game) => {
                            const displayedPlatforms = game.platforms.slice(0, 5);
                            const extraPlatformsCount = game.platforms.length - 5;

                            return (
                                <Card
                                    key={game.id}
                                    sx={{
                                        width: 'calc(33% - 16px)',
                                        borderRadius: 4,
                                        backgroundColor: "#ffffff",
                                        boxShadow: "0 4px 15px rgba(0, 0, 0, 0.1)",
                                        overflow: "hidden",
                                        transition: "transform 0.3s ease, box-shadow 0.3s ease",
                                        "&:hover": {
                                            transform: "translateY(-8px)",
                                            boxShadow: "0 8px 25px rgba(0, 0, 0, 0.2)",
                                        },
                                    }}
                                >
                                    <Link to={`/games/${game.id}`} style={{ textDecoration: "none" }}>
                                        <CardContent
                                            sx={{
                                                padding: 2,
                                                display: "flex",
                                                flexDirection: "column",
                                                justifyContent: "space-between",
                                                height: 'auto',
                                            }}
                                        >
                                            <Typography
                                                variant="h6"
                                                sx={{
                                                    fontWeight: 700,
                                                    marginBottom: 1,
                                                    color: "#333",
                                                }}
                                            >
                                                {game.title}
                                            </Typography>

                                            <Box sx={{ display: "flex", flexWrap: "wrap", gap: 1, marginTop: 1 }}>
                                                {displayedPlatforms.map((platform) => (
                                                    <Chip
                                                        key={platform}
                                                        label={platform}
                                                        variant="outlined"
                                                        sx={{
                                                            textTransform: "capitalize",
                                                            fontWeight: 500,
                                                            fontSize: "0.75rem",
                                                            borderRadius: "12px",
                                                            color: platformColors[platform] || '#808080',
                                                            borderColor: platformColors[platform] || '#808080',
                                                            "&:hover": {
                                                                borderColor: `${platformColors[platform] || '#808080'}80`,
                                                            },
                                                        }}
                                                    />
                                                ))}

                                                {extraPlatformsCount > 0 && (
                                                    <Chip
                                                        label={`+${extraPlatformsCount} more`}
                                                        variant="outlined"
                                                        sx={{
                                                            textTransform: "capitalize",
                                                            fontWeight: 500,
                                                            fontSize: "0.75rem",
                                                            borderRadius: "12px",
                                                            color: '#808080',
                                                            borderColor: '#808080',
                                                            "&:hover": {
                                                                borderColor: '#80808080',
                                                            },
                                                        }}
                                                    />
                                                )}
                                            </Box>
                                        </CardContent>
                                    </Link>
                                </Card>
                            );
                        })}
                    </Box>
                )}
            </Box>

            <Divider sx={{ marginBottom: '16px', borderColor: '#1b263b' }} />

            {/* User Notes Section */}
            <ProfileNotes notes={notes} />

            <Divider sx={{ marginBottom: '16px', borderColor: '#1b263b' }} />

            {/* User Review Section */}
            <GameReviews reviews={reviews} />
        </Box>
    );
}
