import { Link } from "react-router-dom";
import { Card, CardMedia, CardContent, Typography, Box, Chip, Button } from "@mui/material";
import { AddCircle, RemoveCircle } from "@mui/icons-material";
import { GameAPI, User } from "../../types.ts";
import {useToaster} from "../../ToasterContext.tsx";

type GameCardProps = {
    user: User | null;
    game: GameAPI;
    addGameToLibrary: (game: GameAPI) => void;
    deleteGameFromLibrary: (game: GameAPI) => void;
};

export default function GameCard(props: Readonly<GameCardProps>) {
    const { show } = useToaster(); // Use the useToaster hook for notifications

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

    const handleAddGame = () => {
        props.addGameToLibrary(props.game);
        show(`Added ${props.game.title} to library!`, 'success'); // Show success notification
    };

    const handleDeleteGame = () => {
        props.deleteGameFromLibrary(props.game);
        show(`Removed ${props.game.title} from library!`, 'success'); // Show success notification
    };

    const isInLibrary = props.user?.gameLibrary.some(gameInLibrary => gameInLibrary.id === props.game.id); // Check if the game is in the library
    const displayedPlatforms = props.game.platforms.slice(0, 5); // Display up to 5 platforms
    const extraPlatformsCount = props.game.platforms.length - 5; // Count of additional platforms

    return (
        <Card
            variant="outlined"
            sx={{
                width: "100%",
                height: "100%",
                maxHeight: "500px",
                borderRadius: 4,
                backgroundColor: "background.paper",
                boxShadow: "0 4px 15px rgba(0, 0, 0, 0.1)",
                overflow: "hidden",
                display: "flex",
                flexDirection: "column",
                transition: "transform 0.3s ease, box-shadow 0.3s ease",
                "&:hover": {
                    transform: "translateY(-8px)",
                    boxShadow: "0 8px 25px rgba(0, 0, 0, 0.2)",
                },
            }}
        >
            {/* Game cover image */}
            <Link to={`/games/${props.game.id}`} style={{ textDecoration: "none" }}>
                <CardMedia
                    component="img"
                    height={200}
                    image={props.game.coverImage}
                    alt={`${props.game.title} cover image`}
                    sx={{
                        objectFit: "cover",
                        width: "100%",
                        display: "block",
                        transition: "transform 0.5s ease",
                        "&:hover": {
                            transform: "scale(1.05)",
                        },
                    }}
                />
            </Link>

            <CardContent
                sx={{
                    padding: 2,
                    flexGrow: 1,
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "space-between",
                    height: 'auto',
                }}
            >
                <Link to={`/games/${props.game.id}`} style={{ textDecoration: "none" }}>
                    <Typography
                        variant="h6"
                        sx={{
                            fontWeight: 700,
                            marginBottom: 1,
                            color: "primary.contrastText",
                            background: "linear-gradient(45deg, #ffba08, #fca311)",
                            WebkitBackgroundClip: "text",
                            WebkitTextFillColor: "transparent",
                        }}
                    >
                        {props.game.title}
                    </Typography>

                    {/* Display platform chips */}
                    <Box sx={{ display: "flex", flexWrap: "wrap", gap: 1 }}>
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
                                    backgroundColor: 'transparent',
                                    color: platformColors[platform] || '#808080',
                                    borderColor: platformColors[platform] || '#808080',
                                    "&:hover": {
                                        backgroundColor: 'transparent',
                                        borderColor: `${platformColors[platform] || '#808080'}80`,
                                    },
                                }}
                            />
                        ))}

                        {/* Display additional platforms count */}
                        {extraPlatformsCount > 0 && (
                            <Chip
                                label={`+${extraPlatformsCount} more`}
                                variant="outlined"
                                sx={{
                                    textTransform: "capitalize",
                                    fontWeight: 500,
                                    fontSize: "0.75rem",
                                    borderRadius: "12px",
                                    backgroundColor: 'transparent',
                                    color: '#808080',
                                    borderColor: '#808080',
                                    "&:hover": {
                                        backgroundColor: 'transparent',
                                        borderColor: '#80808080',
                                    },
                                }}
                            />
                        )}
                    </Box>
                </Link>

                <Box sx={{ marginTop: 2 }}>
                    {isInLibrary ? (
                        <Button
                            variant="contained"
                            startIcon={<RemoveCircle />}
                            sx={{
                                width: "100%",
                                background: "linear-gradient(45deg, #d9534f, #ff6b6b)",
                                color: "white",
                                fontWeight: 600,
                                boxShadow: "0px 4px 12px rgba(217, 83, 79, 0.3)",
                                "&:hover": {
                                    background: "linear-gradient(45deg, #ff6b6b, #d9534f)",
                                },
                            }}
                            onClick={handleDeleteGame}
                        >
                            Delete from Library
                        </Button>
                    ) : (
                        <Button
                            variant="contained"
                            startIcon={<AddCircle />}
                            sx={{
                                width: "100%",
                                background: "linear-gradient(45deg, #ffba08, #fca311)",
                                color: "white",
                                fontWeight: 600,
                                boxShadow: "0px 4px 12px rgba(255, 186, 8, 0.3)",
                                "&:hover": {
                                    background: "linear-gradient(45deg, #fca311, #ffba08)",
                                },
                            }}
                            onClick={handleAddGame}
                        >
                            Add to Library
                        </Button>
                    )}
                </Box>
            </CardContent>
        </Card>
    );
}
