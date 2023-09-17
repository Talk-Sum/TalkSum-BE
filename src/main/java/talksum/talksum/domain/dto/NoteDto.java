package talksum.talksum.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import talksum.talksum.domain.entity.Member;
import talksum.talksum.domain.entity.Note;

@Getter
@Setter
@ToString
@Builder
public class NoteDto {
    private String title;
    private boolean bookMark;
    private String noteContent;
    private Member author;

    /* DTO -> Entity */
    public Note toEntity(){
        return Note.builder()
                .title(title)
                .bookMark(bookMark)
                .noteContent(noteContent)
                .author(author)
                .build();
    }
}