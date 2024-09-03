import './App.css'
import axios from "axios"
import Footer from "./components/Footer/Footer.tsx";
import {useEffect, useState} from "react";
import {Game, GameLibraryOptions, User} from "./types.ts";
import {CircularProgress} from "@mui/material";
import {Route, Routes} from "react-router-dom";
import GameGallery from "./pages/GameGallery/GameGallery.tsx";
import GameDetail from "./pages/GameDetail/GameDetail.tsx";
import Header from "./components/Header/Header.tsx";
import LandingPage from "./pages/LandingPage/LandingPage.tsx";
import MyLibrary from "./pages/MyLibrary/MyLibrary.tsx";
import Breadcrumbs from "./components/Breadcrumbs/Breadcrumbs.tsx";

const defaultUser: User = {
    id: "0",
    username: "defaulUser",
    gitHubId: "0",
    avatarUrl: "",
    role: "USER",
    gameLibrary: [],
}

function App() {
    const [user, setUser] = useState<User>(defaultUser)
    const [data, setData] = useState<Game[]>([])

    useEffect(() => {
        loadUser()
        fetchGames()
    }, []);

    const loadUser = () => {
        axios.get("api/auth/me")
            .then((response) => {
                setUser(response.data)
            })
            .catch(() => {
                setUser(defaultUser)
            })
    }

    const login = () => {
        const host = window.location.host === 'localhost:5173' ?
            'http://localhost:8080': window.location.origin

        window.open(host + '/oauth2/authorization/github', '_self')
    }

    const logout = () => {
        const host = window.location.host === 'localhost:5173' ?
            'http://localhost:8080' : window.location.origin

        window.open(host + '/logout', '_self')
    }

    const fetchGames = () => {
        axios.get("/api/games")
            .then((response) => {
                setData(response.data)
            })
            .catch((error) => {
                alert(error)
            })
    }

    const addGameToLibrary = (gameId: string) => {
        const dto: GameLibraryOptions  = {
            userId: user.gitHubId,
            gameId: gameId
        }

        axios.put("/api/users/addGame", dto)
            .then(() => {
                loadUser()
            })
    }

    const deleteGameFromLibrary = (gameId: string) => {
        const dto: GameLibraryOptions  = {
            userId: user?.gitHubId ?? '',
            gameId: gameId
        }

        axios.put("/api/users/deleteGame", dto)
            .then(() => {
                loadUser()
            })
    }

    if (data.length === 0 && user !== null) {
        return (
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
                <CircularProgress />
            </div>
        );
    }

    return (
        <div className={"grid"}>
            <Header user={user} onLogin={login} onLogout={logout} />
            <Breadcrumbs/>
            <main>
                <Routes>

                    <Route path="/" element={<LandingPage user={user} onLogin={login}/>} />
                    <Route path="/games" element={<GameGallery games={data}
                                                               user={user}
                                                               addGameToLibrary={addGameToLibrary}
                                                               deleteGameFromLibrary={deleteGameFromLibrary}
                    />}/>
                    <Route path="/games/:id" element={<GameDetail user={user}/>} />
                    <Route path="/my-library" element={<MyLibrary games={data}
                                                                 user={user}
                                                                 addGameToLibrary={addGameToLibrary}
                                                                 deleteGameFromLibrary={deleteGameFromLibrary}
                    />} />
                </Routes>
            </main>
            <Footer />
        </div>
    )
}

export default App
