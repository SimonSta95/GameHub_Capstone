package com.example.gamehubbackend.services;

import com.example.gamehubbackend.exceptions.NoteNotFoundException;
import com.example.gamehubbackend.models.Note;
import com.example.gamehubbackend.models.NoteDTO;
import com.example.gamehubbackend.repositories.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final IdService idService;

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note getNoteById(String id) {
        return noteRepository.findById(id).orElseThrow(() -> new NoteNotFoundException("No note found with id: " + id));
    }

    public Note createNote(NoteDTO noteDTO) {
        Note noteToSave = new Note(
                idService.randomId(),
                noteDTO.userId(),
                noteDTO.gameId(),
                noteDTO.title(),
                noteDTO.content(),
                noteDTO.category()
        );
        return noteRepository.save(noteToSave);
    }

    public Note updateNote(String id, NoteDTO noteDTO) {
        Note noteToUpdate = noteRepository.findById(id).orElseThrow(() -> new NoteNotFoundException("No note found with id: " + id))
                .withUserId(noteDTO.userId())
                .withGameId(noteDTO.gameId())
                .withTitle(noteDTO.title())
                .withContent(noteDTO.content())
                .withCategory(noteDTO.category());

        return noteRepository.save(noteToUpdate);
    }

    public void deleteNote(String id) {
        noteRepository.deleteById(id);
    }
}
