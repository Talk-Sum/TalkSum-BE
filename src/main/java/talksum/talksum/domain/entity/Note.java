package talksum.talksum.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import talksum.talksum.domain.entity.Member;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "note")
@Builder
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noteId")
    private Long noteId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "bookMark", nullable = false)
    private boolean bookMark;

    @Lob
    @Column(name = "noteContent")
    private String noteContent;

    @Column(name = "createdDate", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modifiedDate", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "authorId", referencedColumnName = "loginId", nullable = false)
    private Member author;


    public Note(Long noteId, String title, boolean bookMark, String noteContent, LocalDateTime createdDate, LocalDateTime modifiedDate, Member author) {
        this.noteId = noteId;
        this.title = title;
        this.bookMark = bookMark;
        this.noteContent = noteContent;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.author = author;
    }
}
