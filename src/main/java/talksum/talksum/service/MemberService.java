package talksum.talksum.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import talksum.talksum.domain.dto.MemberDto;
import talksum.talksum.domain.entity.Member;
import talksum.talksum.exception.EmptyDataException;
import talksum.talksum.repository.MemberRepository;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder encoder;

    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder encoder) {
        this.memberRepository = memberRepository;
        this.encoder = encoder;
    }

    /* 회원가입 */
    public Long join(MemberDto memberDto){
        // MemberDto에서 비밀번호 암호화
        memberDto.encryptPassword(encoder.encode(memberDto.getPassword()));

        // MemberDto를 Member 엔티티로 변환
        Member member = memberDto.toEntity();

        // Member 엔티티를 저장하고 저장된 엔티티의 memberId를 반환
        Member savedMember = memberRepository.save(member);
        return savedMember.getMemberId();
    }

    /* 회원 ID로 조회 */
    public Member findMemberById(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EmptyDataException("해당 이름을 가진 유저 데이터가 없음."));
        return member;
    }

    /* 회원 정보 수정 */
    public void updateMember(Long memberId, MemberDto memberParam){
        // 엔터티 조회 후 변경 사항 적용
        Member existingMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new EmptyDataException("해당 ID를 가진 유저 데이터가 없음."));

        // 변경할 필드에 대한 로직을 구현하고, 엔터티에 적용
        // 예: existingMember.setFieldName(memberParam.getFieldName());

        // 변경된 엔터티 저장
        memberRepository.save(existingMember);
    }

    /* 회원 정보 삭제 */
    public void withdrawal(Long memberId){
        // 엔터티 삭제
        memberRepository.deleteById(memberId);
    }

    /* 로그인(return null이면 로그인 실패) */
    public Member login(String loginId, String password){
        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);

        return findMemberOptional.filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
