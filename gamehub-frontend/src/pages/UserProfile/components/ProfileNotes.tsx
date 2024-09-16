import { Note } from "../../../types.ts";
import { Box, Card, CardContent, Chip, Typography } from "@mui/material";

type GameNotesProps = {
    notes: Note[];
};

type ChipColor = "default" | "primary" | "secondary";

// Define colors for note categories
const categoryColors: { [key: string]: ChipColor } = {
    Note: 'primary',
    Goal: 'secondary'
};

export default function ProfileNotes(props: Readonly<GameNotesProps>) {
    const { notes } = props; // Destructure notes from props

    return (
        <Box sx={{ marginTop: '32px' }}>
            <Typography variant="h5" sx={{ fontWeight: 600, marginBottom: 2 }}>
                Your Notes
            </Typography>

            {/* Display existing notes */}
            <Box sx={{ marginBottom: 2 }}>
                {notes.length === 0 ? (
                    <Typography variant="body1">No notes available.</Typography>
                ) : (
                    notes.map((note) => (
                        <Card key={note.id} sx={{ marginBottom: 2, padding: 2, border: '1px solid #ddd' }}>
                            <CardContent>
                                <Typography variant="h5" sx={{ fontWeight: 600 }}>
                                    {note.gameTitle}
                                </Typography>
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
                        </Card>
                    ))
                )}
            </Box>
        </Box>
    );
}
