package com.example.gamehubbackend.services;

import com.example.gamehubbackend.exceptions.NoteNotFoundException;
import com.example.gamehubbackend.models.Note;
import com.example.gamehubbackend.dto.NoteDTO;
import com.example.gamehubbackend.models.UserResponse;
import com.example.gamehubbackend.repositories.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor  // Automatically injects final fields through constructor injection
public class NoteService {

    private final NoteRepository noteRepository;  // Repository for note operations
    private final IdService idService;  // Service to generate unique IDs
    private final UserService userService;

    /**
     * Retrieves all notes from the repository.
     *
     * @return List of all notes
     */
    public List<Note> getAllNotes() {
        return noteRepository.findAll();  // Fetch all notes from the repository
    }

    /**
     * Retrieves a specific note by its ID.
     *
     * @param id the ID of the note to retrieve
     * @return the Note object if found
     * @throws NoteNotFoundException if no note is found with the given ID
     */
    public Note getNoteById(String id) {
        return noteRepository.findById(id)  // Find the note by ID
                .orElseThrow(() -> new NoteNotFoundException("No note found with id: " + id));  // Throw exception if not found
    }

    /**
     * Retrieves all notes by a specific user ID.
     *
     * @param id the user ID
     * @return List of notes associated with the user
     * @throws NoteNotFoundException if no notes are found for the given user ID
     */
    public List<Note> getNoteByUser(String id) {
        return noteRepository.findByUserId(id)  // Find notes by user ID
                .orElseThrow(() -> new NoteNotFoundException("No notes found for id: " + id));  // Throw exception if no notes found
    }

    /**
     * Creates a new note based on the provided NoteDTO.
     *
     * @param noteDTO the data transfer object containing note details
     * @return the newly created Note object
     */
    public Note createNote(NoteDTO noteDTO) {

        Note noteToSave = new Note(
                idService.randomId(),  // Generate a unique ID for the note
                noteDTO.gameTitle(),
                noteDTO.userId(),
                noteDTO.gameId(),
                noteDTO.title(),
                noteDTO.content(),
                noteDTO.category(),
                LocalDateTime.now(),  // Set the creation timestamp to the current time
                LocalDateTime.now()  // Set the updated timestamp to the current time
        );
        return noteRepository.save(noteToSave);
    }

    /**
     * Updates an existing note based on its ID and provided NoteDTO.
     *
     * @param id      the ID of the note to update
     * @param noteDTO the data transfer object containing updated note details
     * @return the updated Note object
     * @throws NoteNotFoundException if no note is found with the given ID
     */
    public Note updateNote(String id, NoteDTO noteDTO) throws AccessDeniedException {

        Note note = getNoteById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User principal = (OAuth2User) authentication.getPrincipal();
            if (!isOwner(note, principal)) {
                throw new AccessDeniedException("Only the owner of the note can delete it.");
            }
        } else {
            String currentUserId = authentication.getName();
            if (!isOwner(note, currentUserId)) {
                throw new AccessDeniedException("Only the owner of the note can delete it.");
            }
        }

        Note noteToUpdate = noteRepository.findById(id)  // Find the note by ID
                .orElseThrow(() -> new NoteNotFoundException("No note found with id: " + id))
                .withGameTitle(noteDTO.gameTitle())
                .withUserId(noteDTO.userId())
                .withGameId(noteDTO.gameId())
                .withTitle(noteDTO.title())
                .withContent(noteDTO.content())
                .withCategory(noteDTO.category())
                .withCreated(noteDTO.created())
                .withUpdated(LocalDateTime.now());  // Update the last modified timestamp to now

        return noteRepository.save(noteToUpdate);
    }

    /**
     * Deletes a note by its ID.
     *
     * @param id the ID of the note to delete
     */
    public void deleteNote(String id) throws AccessDeniedException {

        Note note = getNoteById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User principal = (OAuth2User) authentication.getPrincipal();
            if (!isOwner(note, principal)) {
                throw new AccessDeniedException("Only the owner of the note can delete it.");
            }
        } else {
            String currentUserId = authentication.getName();
            if (!isOwner(note, currentUserId)) {
                throw new AccessDeniedException("Only the owner of the note can delete it.");
            }
        }

        noteRepository.deleteById(id);  // Delete the note from the repository by its ID
    }

    private boolean isOwner(Note note, OAuth2User principal) {
        String githubId = principal.getAttribute("id").toString();
        UserResponse user = userService.getUserByGitHubId(githubId);
        if (user != null) {
            String currentUserId = user.id();
            return note.userId().equals(currentUserId);
        } else {
            // Handle the case where the user is not found
            return false;
        }
    }

    private boolean isOwner(Note note, String currentUserId) {

        UserResponse user = userService.getUserByUsername(currentUserId);
        return note.userId().equals(user.id());
    }
}
