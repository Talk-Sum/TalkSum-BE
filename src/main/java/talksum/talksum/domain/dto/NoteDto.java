package talksum.talksum.domain.dto;

import lombok.*;
import talksum.talksum.domain.entity.Member;
import talksum.talksum.domain.entity.Note;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto {
    private Long noteId;
    private String title;
    private boolean bookMark;
    private boolean isTrash;
    private String noteContent;
    private String audioText;
    private Member author;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


    /* DTO -> Entity */
    public Note toEntity(){
        return Note.builder()
                .noteId(noteId)
                .title(title)
                .bookMark(bookMark)
                .isTrash(isTrash)
                .audioText(audioText)
                .noteContent(noteContent)
                .author(author)
                .build();
    }
}