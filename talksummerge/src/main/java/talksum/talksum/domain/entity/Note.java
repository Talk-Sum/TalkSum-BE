package talksum.talksum.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import talksum.talksum.domain.dto.NoteDto;
import talksum.talksum.domain.entity.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "note")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noteId")
    private Long noteId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "bookMark", nullable = false)
    private boolean bookMark;

    @Column(name = "isTrash", nullable = false)
    private boolean isTrash;

    @Lob
    @Column(name = "noteContent", columnDefinition = "LONGTEXT", nullable = false)
    private String noteContent;

    @Lob
    @Column(name = "audioText", columnDefinition = "LONGTEXT", nullable = false)
    private String audioText;

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

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
        this.modifiedDate = this.createdDate;
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedDate = LocalDateTime.now();
    }

    /* Entity -> DTO */
    public NoteDto toDTO(){
        return NoteDto.builder()
                .noteId(this.noteId)
                .title(this.title)
                .bookMark(this.bookMark)
                .isTrash(this.isTrash)
                .audioText(this.audioText)
                .noteContent(this.noteContent)
                .author(this.author)
                .createdDate(this.createdDate)
                .modifiedDate(this.modifiedDate)
                .build();
    }
}
