import "./Footer.css"
import {Box, Container, Typography} from "@mui/material";
import logo from '../../assets/gamehub-white.png';

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
            <Container className={"footer-container"}
                       maxWidth="lg"
            >
                <img className={"footer-logo"} src={logo} alt={"logo"}/>
                <Typography variant="caption">
                    © {new Date().getFullYear()} Simon Staß. All rights reserved.
                </Typography>
            </Container>
        </Box>
    );
}
