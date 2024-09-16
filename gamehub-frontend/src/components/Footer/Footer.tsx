import "./Footer.css";
import { Box, Typography } from "@mui/material";
import logo from "../../assets/gamehub-white.png";

export default function Footer() {
    return (
        <Box component={"footer"} className={"footer"}>
            {/* Logo image */}
            <img
                className={"footer-logo"}
                src={logo}
                alt={"logo"}
            />

            {/* Footer text indicating the use of the RAWG API */}
            <Typography variant={"caption"} className={"footer-text"}>
                This site uses the RAWG API
            </Typography>

            {/* Footer text with copyright notice */}
            <Typography variant={"caption"} className={"footer-text"}>
                © {new Date().getFullYear()} Simon Staß. All rights reserved.
            </Typography>
        </Box>
    );
}
