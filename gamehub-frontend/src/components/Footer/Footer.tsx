import "./Footer.css"
import {Box, Typography} from "@mui/material";
import logo from "../../assets/gamehub-white.png";

export default function Footer() {
    return (
        <Box component={"footer"} className={"footer"}>
            <img className={"footer-logo"} src={logo} alt={"logo"}/>
            <Typography variant={"caption"} className={"footer-text"}>
                © {new Date().getFullYear()} Simon Staß. All rights reserved.
            </Typography>
        </Box>
    );
}
