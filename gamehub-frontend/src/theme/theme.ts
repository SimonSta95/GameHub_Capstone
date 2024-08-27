import { createTheme, ThemeOptions} from "@mui/material";

const themeOptions: ThemeOptions = {
    palette: {
        primary: {
            main: '#264653', // Dark teal
            light: '#2a9d8f', // Lighter teal
            dark: '#1d3557', // Darker blue (optional for more variations)
            contrastText: '#ffffff', // White text for contrast
        },
        secondary: {
            main: '#e9c46a', // Light orange
            light: '#f4a261', // Lighter orange
            dark: '#e76f51', // Darker orange
            contrastText: '#000000', // Black text for contrast
        },
        background: {
            default: '#f5f5f5', // Light gray background (optional)
        },
    },
    typography: {
        fontFamily: 'Roboto, Arial, sans-serif',
    },
};

const theme = createTheme(themeOptions)

export default theme;