import {Game, Note, User} from "../../../types.ts";
import {
    Box,
    Button,
    Card,
    CardContent,
    CircularProgress,
    IconButton, MenuItem,
    Select,
    TextField,
    Typography
} from "@mui/material";
import { useEffect, useState } from "react";
import axios from "axios";
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Cancel';

type GameNotesProps = {
    game: Game,
    user: User
}

const categories = ["Note", "Goal"]

export default function GameNotes(props: Readonly<GameNotesProps>) {
    const [notes, setNotes] = useState<Note[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [newNote, setNewNote] = useState<{ title: string; content: string; category: string }>({
        title: "",
        content: "",
        category: ""
    });
    const [noteLoading, setNoteLoading] = useState(false);
    const [editingNoteId, setEditingNoteId] = useState<string | null>(null);
    const [editNote, setEditNote] = useState();

    useEffect(() => {
        const fetchNotes = async () => {
            try {
                const response = await axios.get<Note[]>("/api/notes");
                const filteredNotes = response.data.filter(note => note.gameId === props.game.id && note.userId === props.user.gitHubId);
                setNotes(filteredNotes);
            } catch (error) {
                console.error("Error fetching notes:", error);
                setError('Failed to fetch notes.');
            } finally {
                setLoading(false);
            }
        };

        fetchNotes();
    }, [props.game.id, props.user.gitHubId]);

    const handleCreateNote = async () => {
        if (!props.user.gitHubId) {
            console.error('User ID is not available.');
            return;
        }
        if (newNote.title.trim() === "" || newNote.content.trim() === "") {
            console.error('Title and content are required.');
            return;
        }
        setNoteLoading(true);
        try {
            const response = await axios.post<Note>(`/api/notes`, {
                userId: props.user.gitHubId,
                gameId: props.game.id,
                title: newNote.title,
                content: newNote.content,
                category: newNote.category,
            });
            setNotes([...notes, response.data]);
            setNewNote({ title: "", content: "", category: "Note" });
        } catch (error) {
            console.error("Failed to create note:", error);
        } finally {
            setNoteLoading(false);
        }
    };

    const handleDeleteNote = async (noteId: string) => {
        try {
            await axios.delete(`/api/notes/${noteId}`);
            setNotes(notes.filter(note => note.id !== noteId));
        } catch (error) {
            console.error("Failed to delete note:", error);
        }
    };

    const handleEditNote = (note: Note) => {
        setEditingNoteId(note.id);
        setEditNote({
            title: note.title,
            content: note.content,
            category: note.category
        });
    };

    const handleSaveEdit = async (noteId: string) => {
        try {
            const updatedNote = {
                ...editNote,
                userId: props.user.gitHubId,
                gameId: props.game.id,
            };
            const response = await axios.put(`/api/notes/${noteId}`, updatedNote);
            setNotes(notes.map(note => (note.id === noteId ? response.data : note)));
            setEditingNoteId(null);
        } catch (error) {
            console.error("Failed to update note:", error);
        }
    };

    const handleCancelEdit = () => {
        setEditingNoteId(null);
    };

    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                <CircularProgress />
            </Box>
        );
    }

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

            {/* Display existing notes */}
            <Box sx={{ marginBottom: 2 }}>
                {notes.length === 0 ? (
                    <Typography variant="body1">No notes available.</Typography>
                ) : (
                    notes.map((note) => (
                        <Card key={note.id} sx={{ marginBottom: 2, padding: 2, position: 'relative', border: editingNoteId === note.id ? '1px solid #1976d2' : '1px solid #ddd' }}>
                            {editingNoteId === note.id ? (
                                <CardContent>
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
                                        onChange={(e) => setEditNote(prev => ({ ...prev, category: e.target.value as string }))}
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
                                    <Typography variant="h6" sx={{ fontWeight: 600 }}>
                                        {note.title}
                                    </Typography>
                                    <Typography variant="body2" color="textSecondary">
                                        <strong>Category:</strong> {note.category}
                                    </Typography>
                                    <Typography variant="body2" sx={{ marginTop: 1 }}>
                                        {note.content}
                                    </Typography>
                                </CardContent>
                            )}

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

            {/* Add new note form */}
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
                        <em>Category</em> {/* This is the placeholder */}
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