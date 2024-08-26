import "./Footer.css"
import {Box, Container, Typography} from "@mui/material";

export default function Footer() {
    return (
        <Box
            component="footer"
            sx={{
                bgcolor: 'primary.main',
                color: 'primary.contrastText',
                py: 3,
                mt: 'auto',
                textAlign: 'center',
            }}
        >
            <Container className={"footer-container"} maxWidth="lg">
                <img className={"footer-logo"} src={"src/assets/gamehub-white.png"}  alt={"logo"}/>
                <Typography variant="caption text">
                    © {new Date().getFullYear()} Simon Staß. All rights reserved.
                </Typography>
            </Container>
        </Box>
    );
}
