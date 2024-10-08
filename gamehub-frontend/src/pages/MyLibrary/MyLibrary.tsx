import { Box, Typography } from "@mui/material";
import GameCard from "../../components/GameCard/GameCard.tsx";
import { GameAPI, GameAPIResponse, User } from "../../types.ts";

type MyLibraryProps = {
    user: User | null;
    games: GameAPIResponse | null;
    addGameToLibrary: (game: GameAPI) => void;
    deleteGameFromLibrary: (game: GameAPI) => void;
};

export default function MyLibrary(props: Readonly<MyLibraryProps>) {
    // Extract user's game library from props
    const library: GameAPI[] = props.user?.gameLibrary || [];

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
                    display: 'grid',
                    gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))',
                    gap: '16px',
                    justifyContent: 'start',
                    gridAutoFlow: 'row dense',
                }}
            >
                {library.length === 0 ? (
                    // Show a message if the library is empty
                    <Typography variant="h6" sx={{ color: 'text.secondary' }}>
                        No games found in your library.
                    </Typography>
                ) : (
                    // Map through the library and display each game
                    library.map((game) => (
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
