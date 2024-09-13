import { FormEvent, useState } from "react";
import axios from "axios";
import { Box, Button, TextField, Typography, CircularProgress } from '@mui/material';
import {Link, useNavigate} from 'react-router-dom';
import {useToaster} from "../../ToasterContext.tsx";

type loginPageProps = {
    loadUser: () => void;
}

export default function LoginPage(props: Readonly<loginPageProps>) {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [loading, setLoading] = useState(false);

    const { show } = useToaster();
    const navigate = useNavigate();

    const login = () => {
        setLoading(true);
        axios.post("api/auth/login", {}, {
            auth: {
                username: username,
                password: password
            }
        })
            .then(() => {
                setPassword("");
                setUsername("");
                props.loadUser();
                navigate("/");

            })
            .catch(e => {
                setPassword("");
                console.error(e);
                show("Something went wrong!","error")
            })
            .finally(() => setLoading(false));
    }

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        login();
    }

    return (
        <Box
            sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                minHeight: '100vh',
                padding: '16px',
                backgroundColor: '#f5f5f5',
            }}
        >
            <Typography variant="h4" sx={{ marginBottom: '16px' }}>
                Login
            </Typography>
            <Box
                component="form"
                onSubmit={handleSubmit}
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    width: '100%',
                    maxWidth: '400px',
                    padding: '16px',
                    borderRadius: '8px',
                    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
                    backgroundColor: 'white',
                }}
            >
                <TextField
                    label="Username"
                    variant="outlined"
                    margin="normal"
                    fullWidth
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <TextField
                    label="Password"
                    type="password"
                    variant="outlined"
                    margin="normal"
                    fullWidth
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    sx={{ marginTop: '16px' }}
                    disabled={loading}
                >
                    {loading ? <CircularProgress size={24} /> : 'Login'}
                </Button>
            </Box>
            <Box sx={{ marginTop: '24px', textAlign: 'center' }}>
                <Typography variant="body2" sx={{ marginBottom: '8px' }}>
                    Don't have an account? Register here:
                </Typography>
                <Link to="/register" style={{ textDecoration: 'none' }}>
                    <Button
                        variant="contained"
                        color="secondary"
                        sx={{
                            padding: '10px 20px',
                            borderRadius: '8px',
                            fontWeight: 'bold',
                            boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                        }}
                    >
                        Register
                    </Button>
                </Link>
            </Box>
        </Box>
    );
}
