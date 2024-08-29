import {Game} from "../../../../types.ts";
import {Box, Card, CardContent, CardMedia, Chip, Typography} from "@mui/material";
import {Link} from "react-router-dom";

type GameCardProps = {
    game: Game
}

export default function GameCard(props: Readonly<GameCardProps>) {

    return (
        <Link to={`/games/${props.game.id}`} style={{ textDecoration: "none" }}>
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
                </CardContent>
            </Card>
        </Link>
    );
}