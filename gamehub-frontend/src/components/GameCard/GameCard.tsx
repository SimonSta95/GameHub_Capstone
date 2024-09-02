import { Link } from "react-router-dom";
import { Card, CardMedia, CardContent, Typography, Box, Chip, Button } from "@mui/material";
import {AddCircle, RemoveCircle} from "@mui/icons-material";
import {Game, User} from "../../types.ts";

type GameCardProps = {
    user: User
    game: Game
    addGameToLibrary: (gameId: string) => void
    deleteGameFromLibrary: (gameId: string) => void
}

export default function GameCard(props: Readonly<GameCardProps>) {

    const handleAddGame = () => {
        props.addGameToLibrary(props.game.id)
    }

    const handleDeleteGame = () => {
        props.deleteGameFromLibrary(props.game.id)
    }

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
                </Link>

                <Box sx={{ display: "flex", flexWrap: "wrap", gap: 1 }}>
                    {props.game.platforms.map((platform) => (
                        <Chip
                            key={platform}
                            label={platform}
                            variant="outlined"
                            sx={{
                                textTransform: "capitalize",
                                fontWeight: 500,
                                fontSize: "0.75rem",
                                borderRadius: "12px",
                                color: "primary.main",
                                borderColor: "primary.light",
                                "&:hover": {
                                    backgroundColor: "primary.light",
                                    color: "primary.contrastText",
                                },
                            }}
                        />
                    ))}
                </Box>
                <Box sx={{ marginTop: 2 }}>
                    {props.user.gameLibrary.includes(props.game.id) ? (
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
