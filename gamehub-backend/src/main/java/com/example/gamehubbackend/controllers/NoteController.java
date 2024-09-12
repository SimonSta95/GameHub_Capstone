package com.example.gamehubbackend.controllers;

import com.example.gamehubbackend.models.Note;
import com.example.gamehubbackend.models.NoteDTO;
import com.example.gamehubbackend.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;  // Service for handling note operations

    /**
     * Retrieve a list of all notes.
     *
     * @return a list of Note objects
     */
    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    /**
     * Retrieve a specific note by its ID.
     *
     * @param id the ID of the note
     * @return the Note object with the specified ID
     */
    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable String id) {
        return noteService.getNoteById(id);
    }

    /**
     * Create a new note.
     *
     * @param noteDTO the NoteDTO object containing note details
     * @return the newly created Note object
     */
    @PostMapping
    public Note createNote(@RequestBody NoteDTO noteDTO) {
        return noteService.createNote(noteDTO);
    }

    /**
     * Update an existing note.
     *
     * @param id the ID of the note to update
     * @param noteDTO the NoteDTO object containing updated note details
     * @return the updated Note object
     */
    @PutMapping(path = {"{id}/update", "{id}"})
    public Note updateNote(@PathVariable String id, @RequestBody NoteDTO noteDTO) {
        return noteService.updateNote(id, noteDTO);
    }

    /**
     * Delete a note by its ID.
     *
     * @param id the ID of the note to delete
     */
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
    }
}
