import { createTheme, ThemeOptions } from "@mui/material";

const themeOptions: ThemeOptions = {
    palette: {
        primary: {
            main: '#1b263b', // Dark navy blue (main color for header/buttons)
            light: '#415a77', // Muted blue (hover states or borders)
            dark: '#0d1b2a', // Very dark navy (for footer or sections)
            contrastText: '#ffffff', // White text for primary buttons/links
        },
        secondary: {
            main: '#ffba08', // Bright yellow for accents (call to action buttons)
            light: '#fca311', // Softer orange-yellow for interactive elements
            dark: '#d00000', // Bold red for errors or special notifications
            contrastText: '#000000', // Black text for contrast on secondary elements
        },
        background: {
            default: '#f0f0f0', // Light gray for the main background
            paper: '#ffffff', // White for card or modal backgrounds
        },
        error: {
            main: '#e63946', // Bright red for error messages or danger zones
        },
        warning: {
            main: '#ffcc00', // Yellow for warnings
        },
        info: {
            main: '#0077b6', // Bright blue for informational sections
        },
        success: {
            main: '#2a9d8f', // Teal for success states (like notifications or save actions)
        },
    },
    typography: {
        fontFamily: 'Poppins, Roboto, Arial, sans-serif', // Modern font with clean lines
        h1: {
            fontSize: '3rem', // Big headings for key sections
            fontWeight: 700,
        },
        h2: {
            fontSize: '2.5rem',
            fontWeight: 600,
        },
        h3: {
            fontSize: '2rem',
            fontWeight: 500,
        },
        button: {
            textTransform: 'uppercase', // Styled button text for emphasis
            fontWeight: 600,
        },
    },
    components: {
        MuiButton: {
            styleOverrides: {
                root: {
                    borderRadius: '8px', // Slightly rounded for a modern feel
                    padding: '8px 16px',
                },
                containedPrimary: {
                    backgroundColor: '#1b263b',
                    color: '#ffffff',
                    '&:hover': {
                        backgroundColor: '#415a77',
                    },
                },
                containedSecondary: {
                    backgroundColor: '#ffba08',
                    color: '#000000',
                    '&:hover': {
                        backgroundColor: '#fca311',
                    },
                },
            },
        },
    },
};

const theme = createTheme(themeOptions);

export default theme;