import './App.css'
import logo from "./assets/gamehub-color.png"
import axios from "axios"
import Footer from "./components/Footer/Footer.tsx";
import {useEffect, useState} from "react";
import {Game, User} from "./types.ts";
import {Button} from "@mui/material";
import {Route, Routes} from "react-router-dom";
import GameGallery from "./pages/GameGallery/GameGallery.tsx";

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

    return (
      <>
          <img className={"main-logo"} src={logo} alt={"logo"}/>
          <p>{user?.username}</p>
          <div className={"button-container"}>
              <Button variant={"contained"} onClick={login}>GitHub Login</Button>
              <Button variant={"outlined"} onClick={logout}>Logout</Button>
          </div>

          <Routes>
              <Route path={"/games"} element={<GameGallery games={data} />}/>
          </Routes>
          <Footer/>
      </>
    )
}

export default App
