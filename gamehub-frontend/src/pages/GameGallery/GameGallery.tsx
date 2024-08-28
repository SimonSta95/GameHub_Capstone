
import {Game} from "../../types.ts";
import GameCard from "./components/GameCard/GameCard.tsx";
import { Grid } from '@mui/material';

type GameGalleryProps = {
    games: Game[]
}

export default function GameGallery(props: Readonly<GameGalleryProps>) {
    return (
        <Grid container
              spacing={1}
              justifyContent="center"
              sx={{ maxWidth: '1200px', margin: '0 auto' }}>
            {
                props.games.map((game) =>
                <Grid item xs={12} sm={6} md={4} lg={3} key={game.id}>
                    <GameCard key={game.id} game={game} />
                </Grid>
                )
            }
        </Grid>
    );
}