import {Game} from "../../../../types.ts";
import {Card, CardContent, CardHeader, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";


type GameCardProps = {
    game: Game
}

function GameCard(props: Readonly<GameCardProps>) {

    const navigate = useNavigate();

    const goToDetailPage = () => {
        navigate();
    }

    return (
        <button onClick={() => (goToDetailPage)}>
            <Card variant={"outlined"}>
                <CardHeader>
                    <Typography variant={"h5"}>
                        {props.game.title}
                    </Typography>
                </CardHeader>
                <CardContent>

                </CardContent>
            </Card>
        </button>
    );
}

export default GameCard;