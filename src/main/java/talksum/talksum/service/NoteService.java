package talksum.talksum.service;

import talksum.talksum.repository.NoteRepository;

public class NoteService {
    private NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }


}
