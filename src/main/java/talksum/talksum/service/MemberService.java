package talksum.talksum.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import talksum.talksum.domain.dto.MemberDto;
import talksum.talksum.domain.dto.MemberSaveForm;
import talksum.talksum.domain.dto.MemberUpdateForm;
import talksum.talksum.domain.entity.Member;
import talksum.talksum.exception.EmptyDataException;
import talksum.talksum.repository.MemberRepository;

import javax.naming.AuthenticationException;
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
    public Long join(MemberSaveForm memberSaveForm) {
        String name = memberSaveForm.getName();
        String loginId = memberSaveForm.getLoginId();
        String encryptedPassword = encoder.encode(memberSaveForm.getPassword()); // 비밀번호 암호화

        Member member = Member.builder()
                .loginId(memberSaveForm.getLoginId())
                .password(encryptedPassword)
                .name(memberSaveForm.getName())
                .build();
        Member savedMember = memberRepository.save(member);

        return savedMember.getMemberId();
    }

    /* 회원 MemberID로 조회 */
    public Member findMemberById(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EmptyDataException("해당 ID를 가진 유저 데이터가 없음."));
        return member;
    }

    /* 회원 LoginID로 조회 */
    public Member findMemberByLoginId(String loginId){
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EmptyDataException("해당 ID를 가진 유저 데이터가 없음."));
        return member;
    }

    /* 회원 이름으로 조회 */
    public Member findMemberByName(String name){
        Member member = memberRepository.findByName(name)
                .orElseThrow(() -> new EmptyDataException("해당 이름을 가진 유저 데이터가 없음"));
        return member;
    }

    /* 이미 존재하는 로그인 아이디인지 */
    public boolean existsByLoginId(String loginId){
        return memberRepository.findByLoginId(loginId).isPresent();
    }

    /* 이미 존재하는 닉네임인지 */
    public boolean existsByName(String name){
        return memberRepository.findByName(name).isPresent();
    }

    /* 회원 정보 수정 */
    public void updateMember(Long memberId, MemberUpdateForm memberParam){
        // 엔터티 조회 후 변경 사항 적용
        Member existingMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new EmptyDataException("해당 ID를 가진 유저 데이터가 없음."));

        // 변경할 필드에 대한 로직을 구현하고, 엔터티에 적용
        // 예: existingMember.setFieldName(memberParam.getFieldName());

        memberRepository.save(existingMember);
    }

    /* 기존 이름을 새로운 이름으로 수정 */
    public void updateName(String oldName, String newName){
        Member member = memberRepository.findByName(oldName)
                .orElseThrow(() -> new EmptyDataException("해당 이름을 가진 유저 데이터가 없음."));
        memberRepository.updateName(newName, member.getMemberId());
    }

    /* 회원 정보 삭제 */
    public void withdrawal(Long memberId){
        // 엔터티 삭제
        memberRepository.deleteById(memberId);
    }

    /* 로그인 처리 */
    public Member login(String loginId, String password) throws AuthenticationException {
        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);

        if (findMemberOptional.isEmpty()) {
            throw new AuthenticationException("아이디가 존재하지 않습니다.");
        }

        Member member = findMemberOptional.get();

        if (!encoder.matches(password, member.getPassword())) {
            throw new AuthenticationException("비밀번호가 맞지 않습니다.");
        }

        return member;
    }

}
