import { Box, FormControl, InputLabel, MenuItem, Select, TextField, Typography } from '@mui/material';
import GameCard from "../../components/GameCard/GameCard.tsx";
import { Game, User } from "../../types.ts";
import { useState } from "react";

type GameGalleryProps = {
    games: Game[];
    user: User | null;
    addGameToLibrary: (gameId: string) => void;
    deleteGameFromLibrary: (gameId: string) => void;
};

export default function GameGallery(props: Readonly<GameGalleryProps>) {
    const [searchQuery, setSearchQuery] = useState('');
    const [selectedPlatform, setSelectedPlatform] = useState<string>('');

    const platforms = Array.from(new Set(props.games.flatMap(game => game.platforms)));

    const filteredGames = props.games.filter(game =>
        game.title?.toLowerCase().includes(searchQuery?.toLowerCase()) &&
        (selectedPlatform === '' || game.platforms.includes(selectedPlatform))
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

                <FormControl fullWidth sx={{ marginBottom: '16px' }}>
                    <InputLabel>Platform</InputLabel>
                    <Select
                        value={selectedPlatform}
                        onChange={(e) => setSelectedPlatform(e.target.value)}
                        label="Platform"
                        sx={{ borderRadius: '4px', backgroundColor: 'white' }}
                    >
                        <MenuItem value="">All Platforms</MenuItem>
                        {platforms.map(platform => (
                            <MenuItem key={platform} value={platform}>
                                {platform}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
            </Box>

            <Box
                sx={{
                    display: 'grid',
                    gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))',
                    gap: '16px',
                    justifyContent: 'start',
                    gridAutoFlow: 'row dense',
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
                                display: 'flex',
                                flexDirection: 'column',
                                height: '100%',
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
