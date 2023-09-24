package talksum.talksum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import talksum.talksum.domain.dto.MemberDto;
import talksum.talksum.domain.dto.MemberSaveForm;
import talksum.talksum.domain.dto.MemberUpdateForm;
import talksum.talksum.domain.entity.Member;
import talksum.talksum.service.MemberService;


import javax.naming.AuthenticationException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Test
    void loginTest() throws AuthenticationException {
        String loginId = "talksum";
        String password = "talktalk!23";
        Member member = memberService.login(loginId, password);
        assertThat(member).isNotNull();
        assertThat(member.getLoginId()).isEqualTo(loginId);
    }

    /*
    @Test
    void updateMemberTest(){
        String name = "talksum";
        Member findMember = memberService.findMemberByName(name);
        MemberUpdateForm memberUpdateForm = findMember.toDTO();
        memberUpdateForm.setName("talksumsum");
        memberService.updateMember(findMember.getMemberId(), memberUpdateForm);
        assertThat(memberService.findMemberByName("talksumsum")).isNotNull();
    }

     */

    @Test
    void updateNameTest(){
        String name = "talksum";
        Long oldMemberID = memberService.findMemberByName(name).getMemberId();
        memberService.updateName(name, "talksumsum");
        Member updateMember = memberService.findMemberByName("talksumsum");
        assertThat(oldMemberID).isEqualTo(updateMember.getMemberId());
    }
    @Test
    void withdrawalTest(){
        String name = "talksum";
        Member findMember = memberService.findMemberByName(name);
        memberService.withdrawal(findMember.getMemberId());
        memberService.findMemberByName(name); // 예외 발생하면 O
    }


    @Test
    void joinTest(){
        String name = "talksum";
        String loginId = "talksum";
        String password = "talktalk!23";

        MemberSaveForm memberSaveForm = new MemberSaveForm();
        memberSaveForm.setPassword(password);
        memberSaveForm.setName(name);
        memberSaveForm.setLoginId(loginId);

        Long memberId = memberService.join(memberSaveForm);
        Member findMember = memberService.findMemberById(memberId);
    }
}
