package talksum.talksum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import talksum.talksum.domain.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
    // CRUD impl

}
