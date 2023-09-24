package talksum.talksum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import talksum.talksum.domain.entity.Member;
import talksum.talksum.domain.entity.Note;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    // CRUD implement
    Page<Note> findByAuthorAndTitleContainingAndIsTrashFalse(Member author, String keyword, Pageable pageable);
    Page<Note> findByAuthorAndIsTrashTrue(Member author,Pageable pageable);
    Page<Note> findByAuthorAndIsTrashFalse(Member author, Pageable pageable);

    Page<Note> findByAuthorAndBookMarkTrueAndIsTrashFalse(Member author, Pageable pageable);

    @Query("SELECT COUNT(n) FROM Note n WHERE n.author = :author")
    long countNotesByMemberId(Member author);

    Note findByNoteId(Long noteId);

    @Modifying
    @Query("UPDATE Note n SET n.isTrash = :isTrash WHERE n.noteId IN :noteIds")
    void updateIsTrashStatus(@Param("noteIds") List<Long> noteIds, @Param("isTrash") boolean isTrash);

    @Modifying
    @Query("DELETE FROM Note n WHERE n.author = :author AND n.isTrash = :isTrash")
    void deleteNotesByAuthorAndIsTrash(@Param("author") Member author, @Param("isTrash") boolean isTrash);


}
