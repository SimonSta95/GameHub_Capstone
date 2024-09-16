import { editNote, GameDetailAPIResponse, Note, User } from "../../../types.ts";
import {
    Box,
    Button,
    Card,
    CardContent,
    Chip,
    CircularProgress,
    IconButton,
    MenuItem,
    Select,
    TextField,
    Typography,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle
} from "@mui/material";
import { useEffect, useState } from "react";
import axios from "axios";
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Cancel';
import { useToaster } from "../../../ToasterContext.tsx";

type GameNotesProps = {
    game: GameDetailAPIResponse;
    user: User | null;
};

// Define the colors for categories
type ChipColor = "default" | "primary" | "secondary";
const categories = ["Note", "Goal"];
const categoryColors: { [key: string]: ChipColor } = {
    Note: 'primary',
    Goal: 'secondary'
};

export default function GameNotes(props: Readonly<GameNotesProps>) {
    const [notes, setNotes] = useState<Note[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [newNote, setNewNote] = useState<{ title: string; content: string; category: string }>({
        title: "",
        content: "",
        category: "Note"
    });
    const [noteLoading, setNoteLoading] = useState(false);
    const [editingNoteId, setEditingNoteId] = useState<string | null>(null);
    const [editNote, setEditNote] = useState<editNote>({
        gameTitle: props.game.name,
        title: "",
        content: "",
        category: "",
        created: ""
    });
    const [confirmDeleteOpen, setConfirmDeleteOpen] = useState(false);
    const [noteToDelete, setNoteToDelete] = useState<string | null>(null);

    // Get the toaster function from context
    const { show } = useToaster();

    // Fetch notes when component mounts or game/user ID changes
    useEffect(() => {
        const fetchNotes = async () => {
            try {
                // Fetch all notes
                const response = await axios.get<Note[]>("/api/notes");
                // Filter notes to only those associated with the current game and user
                const filteredNotes = response.data.filter(note => note.gameId === props.game.id.toString() && note.userId === props.user?.id);
                setNotes(filteredNotes);
            } catch (error) {
                console.error("Error fetching notes:", error);
                setError('Failed to fetch notes.');
                show('Failed to fetch notes.', 'error'); // Show error toast
            } finally {
                setLoading(false);
            }
        };

        fetchNotes();
    }, [props.game.id, props.user?.id]);

    // Handle creating a new note
    const handleCreateNote = async () => {
        if (!props.user?.id) {
            console.error('User ID is not available.');
            return;
        }
        if (newNote.title.trim() === "" || newNote.content.trim() === "") {
            console.error('Title and content are required.');
            return;
        }
        setNoteLoading(true);
        try {
            // Create a new note via API
            const response = await axios.post<Note>(`/api/notes`, {
                userId: props.user.id,
                name: props.game.name,
                gameId: props.game.id,
                title: newNote.title,
                content: newNote.content,
                category: newNote.category,
            });
            // Update state with the newly created note
            setNotes([...notes, response.data]);
            setNewNote({ title: "", content: "", category: "Note" }); // Reset new note form
            show('Note added successfully!', 'success'); // Show success toast
        } catch (error) {
            console.error("Failed to create note:", error);
            show('Failed to add note.', 'error'); // Show error toast
        } finally {
            setNoteLoading(false);
        }
    };

    // Handle deleting a note
    const handleDeleteNote = (noteId: string) => {
        setNoteToDelete(noteId);
        setConfirmDeleteOpen(true); // Open confirmation dialog
    };

    // Confirm and delete the selected note
    const handleConfirmDelete = async () => {
        if (noteToDelete) {
            try {
                await axios.delete(`/api/notes/${noteToDelete}`);
                setNotes(notes.filter(note => note.id !== noteToDelete)); // Remove deleted note from state
                show('Note deleted successfully!', 'success'); // Show success toast
            } catch (error) {
                console.error("Failed to delete note:", error);
                show('Failed to delete note.', 'error'); // Show error toast
            } finally {
                setConfirmDeleteOpen(false);
                setNoteToDelete(null);
            }
        }
    };

    // Cancel the delete action
    const handleCancelDelete = () => {
        setConfirmDeleteOpen(false);
        setNoteToDelete(null);
    };

    // Handle starting the edit process for a note
    const handleEditNote = (note: Note) => {
        setEditingNoteId(note.id);
        setEditNote({
            gameTitle: props.game.name,
            title: note.title,
            content: note.content,
            category: note.category,
            created: note.created
        });
    };

    // Save changes to an edited note
    const handleSaveEdit = async (noteId: string) => {
        try {
            const updatedNote = {
                ...editNote,
                userId: props.user?.id,
                gameId: props.game.id,
            };
            const response = await axios.put(`/api/notes/${noteId}`, updatedNote);
            // Update state with the edited note
            setNotes(notes.map(note => (note.id === noteId ? response.data : note)));
            setEditingNoteId(null);
            show('Note updated successfully!', 'success'); // Show success toast
        } catch (error) {
            console.error("Failed to update note:", error);
            show('Failed to update note.', 'error'); // Show error toast
        }
    };

    // Cancel editing a note
    const handleCancelEdit = () => {
        setEditingNoteId(null);
    };

    // Show loading spinner while notes are being fetched
    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                <CircularProgress />
            </Box>
        );
    }

    // Show error message if there's an issue fetching notes
    if (error) {
        return (
            <Box sx={{ textAlign: 'center', padding: '24px' }}>
                <Typography variant="h6" color="error">
                    {error}
                </Typography>
            </Box>
        );
    }

    return (
        <Box sx={{ marginTop: '32px' }}>
            <Typography variant="h5" sx={{ fontWeight: 600, marginBottom: 2 }}>
                Notes for this Game
            </Typography>

            {/* Confirmation Dialog for deleting a note */}
            <Dialog
                open={confirmDeleteOpen}
                onClose={handleCancelDelete}
            >
                <DialogTitle>Confirm Delete</DialogTitle>
                <DialogContent>
                    <Typography>Are you sure you want to delete this note?</Typography>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCancelDelete} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={handleConfirmDelete} color="secondary"
                            sx={{ color: 'white', backgroundColor: '#d32f2f', '&:hover': { backgroundColor: '#b71c1c' } }}>
                        Delete
                    </Button>
                </DialogActions>
            </Dialog>

            {/* Display existing notes */}
            <Box sx={{ marginBottom: 2 }}>
                {notes.length === 0 ? (
                    <Typography variant="body1">No notes available.</Typography>
                ) : (
                    notes.map((note) => (
                        <Card key={note.id} sx={{ marginBottom: 2, padding: 2, position: 'relative', border: editingNoteId === note.id ? '1px solid #1976d2' : '1px solid #ddd' }}>
                            {editingNoteId === note.id ? (
                                <CardContent>
                                    {/* Form for editing a note */}
                                    <TextField
                                        required
                                        label="Title"
                                        variant="outlined"
                                        fullWidth
                                        value={editNote.title}
                                        onChange={(e) => setEditNote(prev => ({ ...prev, title: e.target.value }))}
                                        sx={{ mb: 2 }}
                                    />
                                    <Select
                                        value={editNote.category}
                                        onChange={(e) => setEditNote(prev => ({ ...prev, category: e.target.value }))}
                                        fullWidth
                                        sx={{ mb: 2 }}
                                    >
                                        {categories.map(category => (
                                            <MenuItem key={category} value={category}>
                                                {category}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                    <TextField
                                        required
                                        label="Content"
                                        variant="outlined"
                                        multiline
                                        fullWidth
                                        rows={4}
                                        value={editNote.content}
                                        onChange={(e) => setEditNote(prev => ({ ...prev, content: e.target.value }))}
                                        sx={{ mb: 2 }}
                                    />
                                    <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 1 }}>
                                        <IconButton onClick={() => handleSaveEdit(note.id)} color="primary">
                                            <SaveIcon />
                                        </IconButton>
                                        <IconButton onClick={handleCancelEdit} color="error">
                                            <CancelIcon />
                                        </IconButton>
                                    </Box>
                                </CardContent>
                            ) : (
                                <CardContent>
                                    {/* Display a note */}
                                    <Typography variant="h6" sx={{ fontWeight: 600 }}>
                                        {note.title}
                                    </Typography>
                                    <Chip
                                        label={note.category}
                                        color={categoryColors[note.category] || "default"}
                                        sx={{ marginTop: 1 }}
                                    />
                                    <Typography variant="body2" sx={{ marginTop: 1 }}>
                                        {note.content}
                                    </Typography>
                                </CardContent>
                            )}

                            {/* Edit and Delete buttons */}
                            {!editingNoteId && (
                                <Box sx={{ position: 'absolute', top: 8, right: 8 }}>
                                    <IconButton onClick={() => handleEditNote(note)} color="primary">
                                        <EditIcon />
                                    </IconButton>
                                    <IconButton onClick={() => handleDeleteNote(note.id)} color="error">
                                        <DeleteIcon />
                                    </IconButton>
                                </Box>
                            )}
                        </Card>
                    ))
                )}
            </Box>

            {/* Form to add a new note */}
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <TextField
                    required
                    label="Title"
                    variant="outlined"
                    value={newNote.title}
                    onChange={(e) => setNewNote(prev => ({ ...prev, title: e.target.value }))}
                />
                <Select
                    value={newNote.category}
                    onChange={(e) => setNewNote(prev => ({ ...prev, category: e.target.value }))}
                    fullWidth
                    displayEmpty
                >
                    <MenuItem value="" disabled>
                        <em>Category</em>
                    </MenuItem>
                    {categories.map(category => (
                        <MenuItem key={category} value={category}>
                            {category}
                        </MenuItem>
                    ))}
                </Select>
                <TextField
                    required
                    label="Content"
                    variant="outlined"
                    multiline
                    rows={4}
                    value={newNote.content}
                    onChange={(e) => setNewNote(prev => ({ ...prev, content: e.target.value }))}
                />
                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleCreateNote}
                    disabled={noteLoading}
                >
                    {noteLoading ? "Adding..." : "Add Note"}
                </Button>
            </Box>
        </Box>
    );
}
