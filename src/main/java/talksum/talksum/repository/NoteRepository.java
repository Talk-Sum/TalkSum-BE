package talksum.talksum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import talksum.talksum.domain.entity.Note;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    // CRUD implement
    List<Note> findByNoteContentContaining(String keyword);
    List<Note> findByBookMark(boolean bookMark);
}
