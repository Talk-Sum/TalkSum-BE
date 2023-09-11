package talksum.talksum.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Note")
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
    private Date createdDate;

    @Column(name = "modifiedDate", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @ManyToOne
    @JoinColumn(name = "authorId", referencedColumnName = "loginId", nullable = false)
    private User author;

}
