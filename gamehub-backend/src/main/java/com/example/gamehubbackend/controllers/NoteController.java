package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.Note;
import com.example.gamehubbackend.models.NoteDTO;
import com.example.gamehubbackend.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;  // Service for note operations

    /**
     * Get all notes.
     *
     * @return List of all notes
     */
    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    /**
     * Get a note by its ID.
     *
     * @param id the ID of the note
     * @return the Note object corresponding to the given ID
     */
    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable String id) {
        return noteService.getNoteById(id);
    }

    /**
     * Get notes by a specific user ID.
     *
     * @param id the user ID
     * @return List of notes belonging to the specified user
     */
    @GetMapping("/user/{id}")
    public List<Note> getNoteByUser(@PathVariable String id) {
        return noteService.getNoteByUser(id);
    }

    /**
     * Create a new note.
     *
     * @param noteDTO the data transfer object containing the note details
     * @return the created Note object
     */
    @PostMapping
    public Note createNote(@RequestBody NoteDTO noteDTO) {
        return noteService.createNote(noteDTO);
    }

    /**
     * Update an existing note by its ID.
     * Supports two URL paths: "/{id}/update" and "/{id}".
     *
     * @param id the ID of the note to be updated
     * @param noteDTO the data transfer object containing the updated note details
     * @return the updated Note object
     */
    @PutMapping(path = {"{id}/update", "{id}"})
    public Note updateNote(@PathVariable String id, @RequestBody NoteDTO noteDTO) {
        return noteService.updateNote(id, noteDTO);
    }

    /**
     * Delete a note by its ID.
     *
     * @param id the ID of the note to be deleted
     * @return no content and status 204
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
