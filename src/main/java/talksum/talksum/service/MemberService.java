package talksum.talksum.service;

import org.springframework.stereotype.Service;
import talksum.talksum.domain.Member;
import talksum.talksum.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long join(Member member){
        return memberRepository.save(member).getMemberId();
    }

}
