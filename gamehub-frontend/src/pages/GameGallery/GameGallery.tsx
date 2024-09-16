import { Box, FormControl, InputLabel, MenuItem, Select, TextField, Typography, Button, CircularProgress } from '@mui/material';
import GameCard from "../../components/GameCard/GameCard.tsx";
import { GameAPI, GameAPIResponse, User } from "../../types.ts";
import { useState } from "react";
import { useToaster } from "../../ToasterContext.tsx";

type GameGalleryProps = {
    games: GameAPIResponse | null;
    user: User | null;
    fetchGames: (page: number, searchQuery: string) => void;
    addGameToLibrary: (game: GameAPI) => void;
    deleteGameFromLibrary: (game: GameAPI) => void;
};

export default function GameGallery(props: Readonly<GameGalleryProps>) {
    const [searchQuery, setSearchQuery] = useState<string>('');
    const [selectedPlatform, setSelectedPlatform] = useState<string>('');
    const [currentPage, setCurrentPage] = useState<number>(1);
    const [loading, setLoading] = useState<boolean>(false);

    const gamesPerPage = 40;

    const { show } = useToaster();

    // Filter games based on search query and selected platform
    const filteredGames = props.games?.games.filter(game =>
        game.title?.toLowerCase().includes(searchQuery?.toLowerCase()) &&
        (selectedPlatform === '' || game.platforms.includes(selectedPlatform))
    );

    // Handle page change
    const handlePageChange = (newPage: number) => {
        if (newPage !== currentPage) {
            setLoading(true);
            setCurrentPage(newPage);
            try {
                props.fetchGames(newPage, searchQuery); // Fetch games for the new page
            } catch (error) {
                console.error('Failed to fetch games:', error);
                show("Failed to fetch games", "error"); // Show error toast
            } finally {
                setLoading(false);
            }
        }
    };

    // Handle search input change
    const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchQuery(e.target.value);
    };

    // Handle search form submission
    const handleSearchSubmit = () => {
        setLoading(true);
        setCurrentPage(1); // Reset to page 1 on new search
        try {
            props.fetchGames(1, searchQuery); // Fetch games based on new search query
        } catch (error) {
            console.error('Failed to fetch games:', error);
            show("Failed to fetch games", "error"); // Show error toast
        } finally {
            setLoading(false);
        }
    };

    // Slice filtered games to show only the number per page
    const displayedGames = filteredGames?.slice(0, gamesPerPage);

    // Render game cards or a message if no games are found
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
            {/* Search and filter controls */}
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
                    onChange={handleSearchChange}
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
                        {Array.from(new Set(props.games?.games.flatMap(game => game.platforms))).map(platform => (
                            <MenuItem key={platform} value={platform}>
                                {platform}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>

                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleSearchSubmit}
                    disabled={loading}
                >
                    {loading ? <CircularProgress size={24} /> : "Search"}
                </Button>
            </Box>

            {/* Display game cards in a grid layout */}
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
                    renderGameCards()
                )}
            </Box>

            {/* Pagination controls */}
            <Box sx={{ display: 'flex', justifyContent: 'center', marginTop: '16px' }}>
                <Button onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1}>
                    Previous
                </Button>
                <Typography variant="body1" sx={{ margin: '0 16px' }}>
                    Page {currentPage}
                </Typography>
                <Button
                    onClick={() => handlePageChange(currentPage + 1)}
                    disabled={!props.games?.count || currentPage * gamesPerPage >= props.games.count}
                >
                    Next
                </Button>
            </Box>
        </Box>
    );
}
