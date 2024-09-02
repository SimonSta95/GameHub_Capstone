import {Box, Typography} from "@mui/material";
import GameCard from "../../components/GameCard/GameCard.tsx";
import { Game, User } from "../../types.ts";

type MyLibraryProps = {
    user: User;
    games: Game[];
    addGameToLibrary: (gameId: string) => void;
    deleteGameFromLibrary: (gameId: string) => void;
};

export default function MyLibrary(props: Readonly<MyLibraryProps>) {
    const library = props.games.filter((game) => props.user.gameLibrary.includes(game.id));

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
                    <Typography variant="h6" sx={{ color: 'text.secondary' }}>
                        No games found in your library.
                    </Typography>
                ) : (
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
