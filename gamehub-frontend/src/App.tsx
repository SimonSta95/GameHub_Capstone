import './App.css'
import axios from "axios"
import Footer from "./components/Footer/Footer.tsx";
import {useEffect, useState} from "react";
import {User} from "./types.ts";

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
          <h1>App</h1>
          <p>{user?.username}</p>
          <button onClick={login}>GitHub Login</button>
          <button onClick={logout}>Logout</button>
          <Footer/>
      </>
    )
}

export default App
