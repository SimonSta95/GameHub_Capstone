import { Breadcrumbs as MuiBreadcrumbs, Link, Typography, Box } from '@mui/material';
import { Link as RouterLink, useLocation } from 'react-router-dom';

export default function Breadcrumbs() {
    const location = useLocation();
    const pathnames = location.pathname.split('/').filter((x) => x);

    return (
        <Box
            sx={{
                display: 'flex',
                justifyContent: 'flex-start',
                padding: '8px 16px', // Reduced padding for a more compact bar
                backgroundColor: 'rgba(240, 240, 240, 0.8)',  // Soft gray background
                borderRadius: '8px',
                marginBottom: '8px',  // Reduced bottom margin
            }}
        >
            <MuiBreadcrumbs aria-label="breadcrumb" sx={{ fontSize: '0.875rem' }}>  {/* Reduced font size */}
                <Link
                    underline="hover"
                    color="inherit"
                    component={RouterLink}
                    to="/"
                    sx={{
                        fontWeight: '500',
                        color: '#6c757d',  // Subtle gray color for the breadcrumb links
                        "&:hover": { color: '#495057' } // Darker gray on hover
                    }}
                >
                    Home
                </Link>

                {pathnames.map((value, index) => {
                    const to = `/${pathnames.slice(0, index + 1).join('/')}`;
                    const isLast = index === pathnames.length - 1;

                    return isLast ? (
                        <Typography
                            key={to}
                            color="textPrimary"
                            sx={{
                                fontWeight: 'bold',  // Make the last breadcrumb bold
                                color: '#495057',  // Darker gray for the active (last) breadcrumb
                            }}
                        >
                            {value.charAt(0).toUpperCase() + value.slice(1)}
                        </Typography>
                    ) : (
                        <Link
                            key={to}
                            underline="hover"
                            color="inherit"
                            component={RouterLink}
                            to={to}
                            sx={{
                                fontWeight: '500',
                                color: '#6c757d',  // Same subtle gray for intermediate links
                                "&:hover": { color: '#495057' },  // Hover effect for links
                            }}
                        >
                            {value.charAt(0).toUpperCase() + value.slice(1)}
                        </Link>
                    );
                })}
            </MuiBreadcrumbs>
        </Box>
    );
}
