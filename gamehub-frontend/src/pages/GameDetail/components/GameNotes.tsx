import {Game, Note, User} from "../../../types.ts";
import { Box, Button, Card, CardContent, CircularProgress, IconButton, TextField, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import axios from "axios";
import DeleteIcon from '@mui/icons-material/Delete';

type GameNotesProps = {
    game: Game,
    user: User
}

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
            setNewNote({ title: "", content: "", category: "" }); // Clear fields after success
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
                        <Card key={note.id} sx={{ marginBottom: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
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
                            <IconButton onClick={() => handleDeleteNote(note.id)} aria-label="delete" color="error">
                                <DeleteIcon />
                            </IconButton>
                        </Card>
                    ))
                )}
            </Box>

            {/* Add new note form */}
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <TextField
                    label="Title"
                    variant="outlined"
                    value={newNote.title}
                    onChange={(e) => setNewNote(prev => ({ ...prev, title: e.target.value }))}
                />
                <TextField
                    label="Category"
                    variant="outlined"
                    value={newNote.category}
                    onChange={(e) => setNewNote(prev => ({ ...prev, category: e.target.value }))}
                />
                <TextField
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