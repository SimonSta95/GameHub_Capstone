package com.example.gamehubbackend.services;

import com.example.gamehubbackend.exceptions.NoteNotFoundException;
import com.example.gamehubbackend.models.Note;
import com.example.gamehubbackend.models.NoteDTO;
import com.example.gamehubbackend.repositories.NoteRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceUnitTests {
    private final NoteRepository noteRepository = mock(NoteRepository.class);
    private final IdService idService = mock(IdService.class);
    private final NoteService noteService = new NoteService(noteRepository, idService);

    private final LocalDateTime localDateTime = LocalDateTime.parse("2020-01-01T01:00:00");
    private final LocalDateTime updateDateTime = LocalDateTime.parse("2020-01-01T02:00:00");

    @Test
    void getAllNotes_Test() {
        List<Note> notes = List.of(
                new Note("1", "user1", "game1", "Title 1", "Content 1", "category1", localDateTime, updateDateTime),
                new Note("2", "user2", "game2", "Title 2", "Content 2", "category2", localDateTime, updateDateTime)
        );

        when(noteRepository.findAll()).thenReturn(notes);

        List<Note> actualNotes = noteService.getAllNotes();
        assertEquals(notes, actualNotes);
        verify(noteRepository).findAll();
    }

    @Test
    void getNoteById_Test() {
        Note note = new Note("1", "user1", "game1", "Title 1", "Content 1", "category1", localDateTime, updateDateTime);
        when(noteRepository.findById("1")).thenReturn(Optional.of(note));

        Note actualNote = noteService.getNoteById("1");
        assertEquals(note, actualNote);
        verify(noteRepository).findById("1");
    }

    @Test
    void getNoteById_NoNoteFound_Test() {
        when(noteRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> noteService.getNoteById("1"));
        verify(noteRepository).findById("1");
    }

    @Test
    void createNote_Test() {
        NoteDTO noteDTO = new NoteDTO("user1", "game1", "Title 1", "Content 1", "category1", localDateTime, updateDateTime);
        Note noteToSave = new Note("1", "user1", "game1", "Title 1", "Content 1", "category1", LocalDateTime.now(), LocalDateTime.now());

        when(idService.randomId()).thenReturn("1");
        when(noteRepository.save(any(Note.class))).thenReturn(noteToSave);

        Note actualNote = noteService.createNote(noteDTO);
        assertEquals(noteToSave, actualNote);

        verify(idService).randomId();
        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void updateNote_Test() {
        String noteId = "1";
        Note existingNote = new Note("1", "user1", "game1", "Old Title", "Old Content", "category1", localDateTime, updateDateTime);
        NoteDTO updatedNoteDTO = new NoteDTO("user1", "game1", "Updated Title", "Updated Content", "category1", localDateTime, updateDateTime);
        Note updatedNote = new Note("1", "user1", "game1", "Updated Title", "Updated Content", "category1", localDateTime, LocalDateTime.now());

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(Note.class))).thenReturn(updatedNote);

        Note actualNote = noteService.updateNote(noteId, updatedNoteDTO);
        assertEquals(updatedNote, actualNote);

        verify(noteRepository).findById(noteId);
        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void updateNote_NoteNotFound_Test() {
        String noteId = "1";
        NoteDTO updatedNoteDTO = new NoteDTO("user1", "game1", "Updated Title", "Updated Content", "category1", localDateTime, updateDateTime);

        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> noteService.updateNote(noteId, updatedNoteDTO));
        verify(noteRepository).findById(noteId);
        verify(noteRepository, never()).save(any());
    }

    @Test
    void deleteNote_Test() {
        String noteId = "1";

        doNothing().when(noteRepository).deleteById(noteId);
        noteService.deleteNote(noteId);

        verify(noteRepository).deleteById(noteId);
    }
}