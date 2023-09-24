package talksum.talksum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import talksum.talksum.domain.dto.MemberDto;
import talksum.talksum.domain.entity.Member;
import talksum.talksum.repository.MemberRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE) // web환경 사용 X
@Transactional
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void saveTest(){
        MemberDto memberDto = MemberDto.builder()
                .loginId("junha")
                .password("1234")
                .name("junhaa")
                .build();
        Member member = memberDto.toEntity();
        Long member1Id = memberRepository.save(member).getMemberId();

        assertThat(memberRepository.count()).isEqualTo(1);
        MemberDto member1 = memberRepository.getOne(member1Id).toDTO();
        assertThat(member1.getName()).isEqualTo("junhaa");
        assertThat(member1.getPassword()).isEqualTo("1234");
        assertThat(member1.getLoginId()).isEqualTo("junha");
    }


}
