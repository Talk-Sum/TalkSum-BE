package talksum.talksum.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "keyword")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keywordId")
    private Long keywordId;
    @Column(name = "keywords", nullable = false)
    String keywords;

    @ManyToOne
    @JoinColumn(name = "noteId", referencedColumnName = "", nullable = false)
    private Note note;

}
