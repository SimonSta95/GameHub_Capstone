import {Box, TextField, Typography} from '@mui/material';
import GameCard from "../../components/GameCard/GameCard.tsx";
import { Game, User } from "../../types.ts";
import { useState } from "react";

type GameGalleryProps = {
    games: Game[];
    user: User;
    addGameToLibrary: (gameId: string) => void;
    deleteGameFromLibrary: (gameId: string) => void;
};

export default function GameGallery(props: Readonly<GameGalleryProps>) {
    const [searchQuery, setSearchQuery] = useState('');

    // Filter games based on the search query
    const filteredGames = props.games.filter(game =>
        game.title.toLowerCase().includes(searchQuery.toLowerCase())
    );

    return (
        <Box
            sx={{
                maxWidth: '1200px',
                margin: '0 auto',
                padding: '16px',
            }}
        >
            <Box
                sx={{
                    marginBottom: '16px',
                }}
            >
                <TextField
                    fullWidth
                    variant="outlined"
                    placeholder="Search games..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    sx={{
                        marginBottom: '16px',
                        borderRadius: '4px',
                        backgroundColor: 'white',
                    }}
                />
            </Box>

            <Box
                sx={{
                    display: 'flex',
                    flexWrap: 'wrap',
                    justifyContent: 'center',
                    gap: '16px',
                }}
            >
                {filteredGames.length === 0 ? (
                    <Typography variant="h6" sx={{ color: 'text.secondary' }}>
                        No games found.
                    </Typography>
                ) : (
                    filteredGames.map((game) => (
                        <Box
                            key={game.id}
                            sx={{
                                flex: '1 1 calc(25% - 16px)',
                                maxWidth: 'calc(25% - 16px)',
                                minWidth: '280px',
                                boxSizing: 'border-box',
                                height: 'auto',
                            }}
                        >
                            <GameCard
                                game={game}
                                user={props.user}
                                addGameToLibrary={props.addGameToLibrary}
                                deleteGameFromLibrary={props.deleteGameFromLibrary}
                            />
                        </Box>
                    ))
                )}
            </Box>
        </Box>
    );
}