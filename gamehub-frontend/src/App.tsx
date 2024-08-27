import './App.css'
import logo from "./assets/gamehub-color.png"
import axios from "axios"
import Footer from "./components/Footer/Footer.tsx";
import {useEffect, useState} from "react";
import {User} from "./types.ts";
import {Button} from "@mui/material";

function App() {
    const [user, setUser] = useState<User | null | undefined>(undefined)

    useEffect(() => {
        loadUser()
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

    function logout() {
        const host = window.location.host === 'localhost:5173' ?
            'http://localhost:8080' : window.location.origin

        window.open(host + '/logout', '_self')
    }

    return (
      <>
          <img className={"main-logo"} src={logo} alt={"logo"}/>
          <p>{user?.username}</p>
          <Button variant={"contained"} onClick={login}>GitHub Login</Button>
          <Button variant={"outlined"} onClick={logout}>Logout</Button>
          <Footer/>
      </>
    )
}

export default App
