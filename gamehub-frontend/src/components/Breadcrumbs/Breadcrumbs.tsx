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
                padding: '8px 16px',
                backgroundColor: 'rgba(240, 240, 240, 0.8)',
                borderRadius: '8px',
                marginBottom: '8px',
            }}
        >
            <MuiBreadcrumbs aria-label="breadcrumb" sx={{ fontSize: '0.875rem' }}>
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

                {pathnames.map((value, index) => {
                    const to = `/${pathnames.slice(0, index + 1).join('/')}`;
                    const isLast = index === pathnames.length - 1;

                    return isLast ? (
                        <Typography
                            key={to}
                            color="textPrimary"
                            sx={{
                                fontWeight: 'bold',
                                color: '#495057',
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
                                color: '#6c757d',
                                "&:hover": { color: '#495057' },
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
