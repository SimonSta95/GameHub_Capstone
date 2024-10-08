import { FormEvent, useState } from "react";
import axios from "axios";
import { Box, Button, TextField, Typography, CircularProgress } from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import { useToaster } from "../../ToasterContext.tsx";

export default function RegisterPage() {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [confirmedPassword, setConfirmedPassword] = useState<string>("");
    const [loading, setLoading] = useState<boolean>(false);

    const navigate = useNavigate();
    const { show } = useToaster();

    // Function to handle user registration
    const register = () => {
        // Check if passwords match
        if (password !== confirmedPassword) {
            console.error("Passwords do not match");
            show("Passwords do not match", "error");
            return;
        }

        setLoading(true);
        axios.post("api/auth/register", {
            username: username,
            password: password,
        })
            .then(() => {
                // Clear input fields and navigate to home
                setPassword("");
                setUsername("");
                setConfirmedPassword("");
                navigate("/");
            })
            .catch(e => {
                // Clear password fields and show error message
                setPassword("");
                console.error(e);
                show("Something went wrong!", "error");
            })
            .finally(() => setLoading(false));
    };

    // Function to handle form submission
    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault(); // Prevent default form submission
        register();
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
                Register
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
                <TextField
                    label="Confirm Password"
                    type="password"
                    variant="outlined"
                    margin="normal"
                    fullWidth
                    value={confirmedPassword}
                    onChange={(e) => setConfirmedPassword(e.target.value)}
                />
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    sx={{ marginTop: '16px' }}
                    disabled={loading} // Disable button while loading
                >
                    {loading ? <CircularProgress size={24} /> : 'Register'}
                </Button>
            </Box>
            <Box sx={{ marginTop: '24px', textAlign: 'center' }}>
                <Typography variant="body2" sx={{ marginBottom: '8px' }}>
                    Already have an account? Log in here:
                </Typography>
                <Link to="/login" style={{ textDecoration: 'none' }}>
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
                        Log In
                    </Button>
                </Link>
            </Box>
        </Box>
    );
}
