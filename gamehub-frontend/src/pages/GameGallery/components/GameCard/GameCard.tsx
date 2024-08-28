import {Game} from "../../../../types.ts";
import {Box, Card, CardContent, CardMedia, Chip, Typography} from "@mui/material";
import {Link} from "react-router-dom";

type GameCardProps = {
    game: Game
}

function GameCard(props: Readonly<GameCardProps>) {

    return (
        <Link to={`/games/${props.game.id}`} style={{ textDecoration: 'none' }}>
            <Card
                variant="outlined"
                sx={{
                    width: '100%',
                    height: '400px',
                    borderRadius: 4,
                    boxShadow: '0 4px 20px rgba(0, 0, 0, 0.1)',
                    overflow: 'hidden',
                    transition: 'transform 0.3s ease, box-shadow 0.3s ease',
                    '&:hover': {
                        transform: 'translateY(-10px)',
                        boxShadow: '0 8px 30px rgba(0, 0, 0, 0.2)',
                    }
                }}
            >
                <CardMedia
                    component="img"
                    height={250}
                    image={props.game.coverImage}
                    alt={`${props.game.title} cover image`}
                    sx={{
                        objectFit: 'cover',
                        width: '100%',
                        transition: 'transform 0.5s ease',
                        '&:hover': {
                            transform: 'scale(1.05)',
                        }
                    }}
                />

                <CardContent sx={{
                    padding: 3,
                    transition: 'opacity 0.3s ease-in-out',
                    '&:hover': {
                        opacity: 0.85,
                    }
                }}>
                    <Typography
                        variant="h5"
                        sx={{
                            fontWeight: 700,
                            marginBottom: 2,
                            background: 'linear-gradient(45deg, #f39c12, #d35400)',
                            WebkitBackgroundClip: 'text',
                            WebkitTextFillColor: 'transparent'
                        }}
                    >
                        {props.game.title}
                    </Typography>

                    <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
                        {props.game.platforms.map((platform) => (
                            <Chip
                                key={platform}
                                label={platform}
                                variant="outlined"
                                sx={{
                                    textTransform: 'capitalize',
                                    fontWeight: 500,
                                    fontSize: '0.75rem',
                                    borderRadius: '12px',
                                }}
                            />
                        ))}
                    </Box>
                </CardContent>
            </Card>
        </Link>
    );
}

export default GameCard;