import './App.css'
import axios from "axios"
import Footer from "./components/Footer/Footer.tsx";
import {useEffect, useState} from "react";
import {Game, User} from "./types.ts";
import {CircularProgress} from "@mui/material";
import {Route, Routes} from "react-router-dom";
import GameGallery from "./pages/GameGallery/GameGallery.tsx";
import GameDetail from "./pages/GameDetail/GameDetail.tsx";
import Header from "./components/Header/Header.tsx";
import LandingPage from "./pages/LandingPage/LandingPage.tsx";

function App() {
    const [user, setUser] = useState<User | null | undefined>(undefined)
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
                setUser(null)
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
        axios.get("api/games")
            .then((response) => {
                setData(response.data)
            })
            .catch((error) => {
                alert(error)
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
            <main>
                <Routes>
                    <Route path="/" element={<LandingPage user={user} onLogin={login}/>} />
                    <Route path="/games" element={<GameGallery games={data} />} />
                    <Route path="/games/:id" element={<GameDetail />} />
                </Routes>
            </main>
            <Footer />
        </div>
    )
}

export default App
