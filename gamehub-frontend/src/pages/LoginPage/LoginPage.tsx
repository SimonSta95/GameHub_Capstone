import { FormEvent, useState } from "react";
import axios from "axios";
import { Box, Button, TextField, Typography, CircularProgress } from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import { useToaster } from "../../ToasterContext.tsx";

type LoginPageProps = {
    loadUser: () => void;
};

export default function LoginPage(props: Readonly<LoginPageProps>) {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [loading, setLoading] = useState<boolean>(false);

    const { show } = useToaster();
    const navigate = useNavigate();

    // Function to handle login process
    const login = () => {
        setLoading(true); // Start loading spinner
        axios.post("api/auth/login", {}, {
            auth: {
                username: username,
                password: password
            }
        })
            .then(() => {
                // On successful login
                setPassword("");
                setUsername("");
                props.loadUser();
                navigate("/");
            })
            .catch(e => {
                // On login error
                setPassword("");
                console.error(e);
                show("Something went wrong!", "error"); // Show error notification
            })
            .finally(() => setLoading(false));
    };

    // Function to handle form submission
    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault(); // Prevent default form submission
        login();
    };

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
