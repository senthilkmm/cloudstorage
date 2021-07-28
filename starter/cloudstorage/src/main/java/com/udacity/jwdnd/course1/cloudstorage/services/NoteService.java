package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getNotes(Integer userId) {
        return noteMapper.getAllNotes(userId);
    }

    public void addNote(Note note, Integer userId) {
        note.setUserId(userId);
        noteMapper.addNote(note);
    }

    public void deleteNote(int id) {
        noteMapper.deleteNote(id);
    }

    public Note getNote(Integer noteId) {
        return noteMapper.getNote(noteId);
    }

    public void updateNote(Note note) {
        noteMapper.updateNote(note);
    }
}
