import { Snackbar, Alert } from '@mui/material';
import { createContext, ReactNode, useCallback, useContext, useMemo, useState } from 'react';

type ToasterContextType = {
    show: (message: string, severity: 'success' | 'error') => void;
};

const ToasterContext = createContext<ToasterContextType | undefined>(undefined);

interface ToasterProviderProps {
    children: ReactNode;
}

export default function ToasterProvider({ children }: Readonly<ToasterProviderProps>) {  // Fix here
    const [open, setOpen] = useState(false);
    const [message, setMessage] = useState('');
    const [severity, setSeverity] = useState<'success' | 'error'>('success');

    // Wrap the 'show' function in useCallback to ensure it doesn't change on every render
    const show = useCallback((message: string, severity: 'success' | 'error') => {
        setMessage(message);
        setSeverity(severity);
        setOpen(true);
    }, []);

    const handleClose = () => {
        setOpen(false);
    };

    // Memoize the value object to prevent unnecessary recalculations
    const value = useMemo(() => ({ show }), [show]);

    return (
        <ToasterContext.Provider value={value}>
            {children}
            <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                <Alert onClose={handleClose} severity={severity} sx={{ width: '100%' }}>
                    {message}
                </Alert>
            </Snackbar>
        </ToasterContext.Provider>
    );
};

export const useToaster = () => {
    const context = useContext(ToasterContext);
    if (!context) {
        throw new Error('useToaster must be used within a ToasterProvider');
    }
    return context;
};
