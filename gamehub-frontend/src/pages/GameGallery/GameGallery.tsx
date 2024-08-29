
import {Game} from "../../types.ts";
import GameCard from "./components/GameCard/GameCard.tsx";
import {Box} from '@mui/material';

type GameGalleryProps = {
    games: Game[]
}

export default function GameGallery(props: Readonly<GameGalleryProps>) {
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
                    display: 'flex',
                    flexWrap: 'wrap',
                    justifyContent: 'center',
                    gap: '16px', // Space between items
                }}
            >
                {props.games.map((game) => (
                    <Box
                        key={game.id}
                        sx={{
                            flex: '1 1 calc(25% - 16px)', // 4 items per row with gap adjustment
                            maxWidth: 'calc(25% - 16px)', // 4 items per row with gap adjustment
                            minWidth: '280px', // Adjust as needed for responsiveness
                            boxSizing: 'border-box', // Include padding and border in the element's total width and height
                        }}
                    >
                        <GameCard game={game} />
                    </Box>
                ))}
            </Box>
        </Box>
    );
}