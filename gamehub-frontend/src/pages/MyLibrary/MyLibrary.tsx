import {Box} from "@mui/material";
import GameCard from "../../components/GameCard/GameCard.tsx";
import {Game, User} from "../../types.ts";


type MyLibraryProps = {
    user: User,
    games: Game[],
    addGameToLibrary: (gameId: string) => void,
    deleteGameFromLibrary: (gameId: string) => void
}

export default function MyLibrary(props: Readonly<MyLibraryProps>) {

    const library = props.games.filter((game) => props.user.gameLibrary.includes(game.id));

    return(
        <Box
            sx={{
                maxWidth: '1200px',
                margin: '0 auto',
                padding: '16px',
            }}
        >
            <Box
                sx={{
                    display: 'flex',
                    flexWrap: 'wrap',
                    justifyContent: 'center',
                    gap: '16px',
                }}
            >
                {library.map((game) => (
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

                        <GameCard game={game}
                                  user={props.user}
                                  addGameToLibrary={props.addGameToLibrary}
                                  deleteGameFromLibrary={props.deleteGameFromLibrary}
                        />
                    </Box>
                ))}
            </Box>
        </Box>
    )
}