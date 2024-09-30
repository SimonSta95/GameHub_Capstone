import axios from "axios";
import {
    Box,
    FormControl,
    MenuItem,
    Select,
    TextField,
    Typography,
    CircularProgress,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    IconButton
} from '@mui/material';
import { useState, useEffect } from "react";
import { useToaster } from "../../ToasterContext.tsx";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { User } from "../../types.ts";



function AdminPage() {
    const [users, setUsers] = useState<User[]>([]);
    const [searchQuery, setSearchQuery] = useState<string>('');
    const [loadingUsers, setLoadingUsers] = useState<boolean>(false);
    const [selectedRoles, setSelectedRoles] = useState<Record<string, string>>({});

    const { show } = useToaster();

    // Fetch users from API on load
    useEffect(() => {
        fetchAllUsers();
    }, []);

    // Fetch all users function
    const fetchAllUsers = async () => {
        setLoadingUsers(true);
        try {
            const response = await axios.get("/api/users");
            setUsers(response.data);
        } catch (error) {
            console.error(error)
            show("Failed to fetch users", "error");
        } finally {
            setLoadingUsers(false);
        }
    };

    // Delete user function
    const deleteUser = async (userId: string) => {
        try {
            await axios.delete(`/api/users/${userId}`);
            show("User deleted successfully", "success");
            fetchAllUsers();  // Re-fetch the users after deleting one
        } catch (error) {
            console.error(error)
            show("Failed to delete user", "error");
        }
    };

    // Filter users based on search query
    const filteredUsers = users.filter(user =>
        user.username.toLowerCase().includes(searchQuery.toLowerCase())
    );

    // Handle search input change
    const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchQuery(e.target.value);
    };

    return (
        <Box sx={{ maxWidth: '1200px', margin: '0 auto', padding: '16px' }}>
            {/* User Management Section */}
            <Box sx={{ marginBottom: '16px' }}>
                <Typography variant="h5" gutterBottom>
                    User Management
                </Typography>

                {/* Search bar for users */}
                <TextField
                    fullWidth
                    variant="outlined"
                    placeholder="Search users..."
                    value={searchQuery}
                    onChange={handleSearchChange}
                    sx={{
                        marginBottom: '16px',
                        borderRadius: '4px',
                        backgroundColor: 'white',
                    }}
                />

                {loadingUsers ? (
                    <CircularProgress />
                ) : (
                    <TableContainer component={Paper}>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>ID</TableCell>
                                    <TableCell>Name</TableCell>
                                    <TableCell>Email</TableCell>
                                    <TableCell>Role</TableCell>
                                    <TableCell>Actions</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {filteredUsers.map((user) => (
                                    <TableRow key={user.id}>
                                        <TableCell>{user.id}</TableCell>
                                        <TableCell>{user.username}</TableCell>
                                        <TableCell>{user.gitHubId}</TableCell>
                                        <TableCell>
                                            <FormControl fullWidth>
                                                <Select
                                                    sx={{ backgroundColor: 'white' }}
                                                    value={selectedRoles[user.id] || user.role}
                                                >
                                                    <MenuItem value="user">User</MenuItem>
                                                    <MenuItem value="moderator">Moderator</MenuItem>
                                                    <MenuItem value="admin">Admin</MenuItem>
                                                </Select>
                                            </FormControl>
                                        </TableCell>
                                        <TableCell>
                                            <IconButton
                                                color="primary"
                                                aria-label="edit role"
                                            >
                                                <EditIcon />
                                            </IconButton>
                                            <IconButton
                                                onClick={() => deleteUser(user.id)}
                                                color="secondary"
                                                aria-label="delete user"
                                            >
                                                <DeleteIcon />
                                            </IconButton>
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                )}
            </Box>
        </Box>
    );
}

export default AdminPage;