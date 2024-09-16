import { Breadcrumbs as MuiBreadcrumbs, Link, Typography, Box } from '@mui/material';
import { Link as RouterLink, useLocation } from 'react-router-dom';

export default function Breadcrumbs() {
    // Get the current location from the router
    const location = useLocation();

    // Split the pathname into segments and filter out empty values
    const pathnames = location.pathname.split('/').filter((x) => x);

    return (
        <Box
            sx={{
                display: 'flex',
                justifyContent: 'flex-start',
                padding: '8px 16px',
                backgroundColor: 'rgba(240, 240, 240, 0.8)',
                borderRadius: '8px',
                marginBottom: '8px',
            }}
        >
            <MuiBreadcrumbs aria-label="breadcrumb" sx={{ fontSize: '0.875rem' }}>
                {/* Link to the homepage */}
                <Link
                    underline="hover"
                    color="inherit"
                    component={RouterLink}
                    to="/"
                    sx={{
                        fontWeight: '500',
                        color: '#6c757d',
                        "&:hover": { color: '#495057' }
                    }}
                >
                    Home
                </Link>

                {/* Generate breadcrumbs for each segment of the path */}
                {pathnames.map((value, index) => {
                    // Create a path up to the current segment
                    const to = `/${pathnames.slice(0, index + 1).join('/')}`;
                    // Check if the current segment is the last one
                    const isLast = index === pathnames.length - 1;

                    return isLast ? (
                        // Render the last segment as a non-link item
                        <Typography
                            key={to}
                            color="textPrimary"
                            sx={{
                                fontWeight: 'bold',
                                color: '#495057',
                            }}
                        >
                            {value.charAt(0).toUpperCase() + value.slice(1)} {/* Capitalize the first letter */}
                        </Typography>
                    ) : (
                        // Render other segments as links
                        <Link
                            key={to}
                            underline="hover"
                            color="inherit"
                            component={RouterLink}
                            to={to}
                            sx={{
                                fontWeight: '500',
                                color: '#6c757d',
                                "&:hover": { color: '#495057' },
                            }}
                        >
                            {value.charAt(0).toUpperCase() + value.slice(1)} {/* Capitalize the first letter */}
                        </Link>
                    );
                })}
            </MuiBreadcrumbs>
        </Box>
    );
}
