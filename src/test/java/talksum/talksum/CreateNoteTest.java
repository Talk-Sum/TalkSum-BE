package talksum.talksum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import talksum.talksum.domain.dto.MemberSaveForm;
import talksum.talksum.domain.dto.NoteDto;
import talksum.talksum.domain.entity.Member;
import talksum.talksum.domain.entity.Note;
import talksum.talksum.service.MemberService;
import talksum.talksum.service.NoteService;

@SpringBootTest
public class CreateNoteTest {
    @Autowired
    private NoteService noteService;

    @Autowired
    private MemberService memberService;



    @Test
    void createNote(){
        int N = 20; // 생성할 노트 개수
        for(int i = 1 ; i <= N ; i ++){
            NoteDto noteDto = NoteDto.builder()
                    .bookMark(false)
                    .isTrash(false)
                    .noteContent("Tes\ntT\nest\nTe\nstTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTes\n" +
                            "tTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestT\n" +
                            "estTestTestTestTestTestTestTestTestTestTestTest " + String.valueOf(i))
                    .title("Title " + String.valueOf(i))
                    .audioText("AudioText " + String.valueOf(i))
                    .author(memberService.findMemberByName("test"))
                    .build();

            noteService.saveNote(noteDto);
        }

    }
}
