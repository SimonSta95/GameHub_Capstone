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

    private final NoteService noteService;

    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable String id) {
        return noteService.getNoteById(id);
    }

    @GetMapping("/user/{id}")
    public List<Note> getNoteByUser(@PathVariable String id) {
        return noteService.getNoteByUser(id);
    }

    @PostMapping
    public Note createNote(@RequestBody NoteDTO noteDTO) {
        return noteService.createNote(noteDTO);
    }

    @PutMapping(path = {"{id}/update", "{id}"})
    public Note updateNote(@PathVariable String id ,@RequestBody NoteDTO noteDTO) {
        return noteService.updateNote(id,noteDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
    }
}
