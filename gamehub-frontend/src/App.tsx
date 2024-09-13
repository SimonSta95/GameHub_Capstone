import './App.css'
import axios from "axios";
import Footer from "./components/Footer/Footer.tsx";
import { useEffect, useState } from "react";
import { UserGameLibraryAction, GameAPI, GameAPIResponse, User } from "./types.ts";
import { CircularProgress } from "@mui/material";
import { Route, Routes } from "react-router-dom";
import GameGallery from "./pages/GameGallery/GameGallery.tsx";
import GameDetail from "./pages/GameDetail/GameDetail.tsx";
import Header from "./components/Header/Header.tsx";
import LandingPage from "./pages/LandingPage/LandingPage.tsx";
import MyLibrary from "./pages/MyLibrary/MyLibrary.tsx";
import Breadcrumbs from "./components/Breadcrumbs/Breadcrumbs.tsx";
import LoginPage from "./pages/LoginPage/LoginPage.tsx";
import RegisterPage from "./pages/RegisterPage/RegisterPage.tsx";
import UserProfile from "./pages/UserProfile/UserProfile.tsx";
import {useToaster} from "./ToasterContext.tsx";


function App() {
    const [user, setUser] = useState<User | null>(null);
    const [data, setData] = useState<GameAPIResponse | null>(null);

    const { show } = useToaster(); // Use toaster context

    useEffect(() => {
        loadUser();
        fetchGames(1, "");
    }, []);

    const loadUser = () => {
        axios.get("/api/auth/me")
            .then((response) => {
                setUser(response.data);
                show("User loaded successfully!", 'success'); // Toaster success
                fetchGames(1, "");
            })
            .catch(() => {
                setUser(null);
                show("Failed to load user.", 'error'); // Toaster error
            });
    };

    const login = () => {
        const host = window.location.host === 'localhost:5173' ?
            'http://localhost:8080' : window.location.origin;

        window.open(host + '/oauth2/authorization/github', '_self');
    };

    const logout = () => {
        const host = window.location.host === 'localhost:5173' ?
            'http://localhost:8080' : window.location.origin;

        window.open(host + '/logout', '_self');
    };

    const fetchGames = (page: number, searchQuery: string) => {
        axios.get(`/api/games/fetch?page=${page}&search=${encodeURIComponent(searchQuery)}`)
            .then((response) => {
                setData(response.data);
                show("Games fetched successfully!", 'success'); // Toaster success
            })
            .catch(() => {
                show("Failed to fetch games.", 'error'); // Toaster error
            });
    };

    const addGameToLibrary = (game: GameAPI) => {
        if (!user) return;
        const dto: UserGameLibraryAction = {
            userId: user.gitHubId,
            game: game
        };

        axios.put("/api/users/addGame", dto)
            .then(() => {
                loadUser();
                show("Game added to library!", 'success'); // Toaster success
            })
            .catch(() => {
                show("Failed to add game to library.", 'error'); // Toaster error
            });
    };

    const deleteGameFromLibrary = (game: GameAPI) => {
        if (!user) return;
        const dto: UserGameLibraryAction = {
            userId: user?.gitHubId ?? '',
            game: game
        };

        axios.put("/api/users/deleteGame", dto)
            .then(() => {
                loadUser();
                show("Game removed from library!", 'success'); // Toaster success
            })
            .catch(() => {
                show("Failed to remove game from library.", 'error'); // Toaster error
            });
    };

    if (!data && user !== null) {
        return (
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
                <CircularProgress />
            </div>
        );
    }

    return (
        <div className={"grid"}>
            <Header user={user} onLogin={login} onLogout={logout} />
            <Breadcrumbs />
            <main>
                <Routes>
                    <Route path="/" element={<LandingPage user={user} onLogin={login} />} />
                    <Route path="/login" element={<LoginPage loadUser={loadUser} />} />
                    <Route path="/register" element={<RegisterPage />} />
                    <Route path="/games" element={<GameGallery
                        games={data}
                        user={user}
                        addGameToLibrary={addGameToLibrary}
                        deleteGameFromLibrary={deleteGameFromLibrary}
                        fetchGames={fetchGames}
                    />} />
                    <Route path="/games/:id" element={<GameDetail user={user} />} />
                    <Route path="/my-library" element={<MyLibrary
                        games={data}
                        user={user}
                        addGameToLibrary={addGameToLibrary}
                        deleteGameFromLibrary={deleteGameFromLibrary}
                    />} />
                    <Route path="/user/:id" element={<UserProfile />} />
                </Routes>
            </main>
            <Footer />
        </div>
    );
}

export default App;
