import { Box, FormControl, InputLabel, MenuItem, Select, TextField, Typography, Button, CircularProgress } from '@mui/material';
import GameCard from "../../components/GameCard/GameCard.tsx";
import { GameAPIResponse, User } from "../../types.ts";
import { useState } from "react";

type GameGalleryProps = {
    games: GameAPIResponse | null;
    user: User | null;
    fetchGames: (page: number) => void;
    addGameToLibrary: (gameId: string) => void;
    deleteGameFromLibrary: (gameId: string) => void;
};

export default function GameGallery(props: Readonly<GameGalleryProps>) {
    const [searchQuery, setSearchQuery] = useState('');
    const [selectedPlatform, setSelectedPlatform] = useState<string>('');
    const [currentPage, setCurrentPage] = useState(1);
    const [loading, setLoading] = useState(false);

    const gamesPerPage = 40;

    // Handle filtering on the client side (this will not trigger a new fetch)
    const filteredGames = props.games?.games.filter(game =>
        game.title?.toLowerCase().includes(searchQuery?.toLowerCase()) &&
        (selectedPlatform === '' || game.platforms.includes(selectedPlatform))
    );

    const handlePageChange = (newPage: number) => {
        if (newPage !== currentPage) {
            setLoading(true); // Show loading while fetching new page
            setCurrentPage(newPage);
            props.fetchGames(newPage); // Only fetch when page changes
            setLoading(false);
        }
    };

    // Calculate displayed games for the current page
    const displayedGames = filteredGames?.slice(0, gamesPerPage); // Always limit to first 40

    // Extracted content display logic into an independent statement
    const renderGameCards = () => {
        if (!displayedGames || displayedGames.length === 0) {
            return (
                <Typography variant="h6" sx={{ color: 'text.secondary' }}>
                    No games found.
                </Typography>
            );
        }

        return displayedGames.map((game) => (
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
        ));
    };

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
                        {/* Render platform options */}
                        {Array.from(new Set(props.games?.games.flatMap(game => game.platforms))).map(platform => (
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
                {loading ? (
                    <CircularProgress />
                ) : (
                    renderGameCards()  // Use the extracted function to render games
                )}
            </Box>

            {/* Pagination buttons */}
            <Box sx={{ display: 'flex', justifyContent: 'center', marginTop: '16px' }}>
                <Button onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1}>
                    Previous
                </Button>
                <Typography variant="body1" sx={{ margin: '0 16px' }}>
                    Page {currentPage}
                </Typography>
                <Button
                    disabled={!!props.games?.count && currentPage * gamesPerPage >= props.games.count}
                >
                    Next
                </Button>
            </Box>
        </Box>
    );
}