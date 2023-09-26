package talksum.talksum;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import talksum.talksum.domain.dto.NoteDto;
import talksum.talksum.domain.entity.Member;
import talksum.talksum.repository.NoteRepository;
import talksum.talksum.service.MemberService;
import talksum.talksum.service.NoteService;

import java.util.List;

@SpringBootTest
public class NoteServiceTest {

    @Autowired
    NoteService noteService;

    @Autowired
    MemberService memberService;

    @Test
    void toggleTest() throws Exception {
        NoteDto noteDto = noteService.getNote((long)236);
        System.out.println(noteDto.getNoteContent());
    }

    @Test
    void getNoteCount(){
        Member member = memberService.findMemberByLoginId("test");

        System.out.println(noteService.getNoteListCount(member));
    }

    /*
    @Test
    void getBookmarkListTest(){
        Member member = memberService.findMemberByLoginId("test");
        List<NoteDto> noteDtoList = noteService.getUserBookMarkNote(member);
        for(NoteDto note : noteDtoList){
            Assertions.assertThat(note.isBookMark()).isTrue();
        }
    }

     */
}
