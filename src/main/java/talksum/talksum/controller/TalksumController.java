package talksum.talksum.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import talksum.talksum.domain.Member;
import talksum.talksum.service.STTservice;
import talksum.talksum.service.MemberService;

@RestController
public class TalksumController {

    private final STTservice sttservice;
    private final MemberService memberService;
    public TalksumController(STTservice sttservice, MemberService memberService) {
        this.sttservice = sttservice;
        this.memberService = memberService;
    }

    // DTO로 변환 후 리턴 기능 추가하기
    @GetMapping("/createUser")
    public Long createUser(String loginId, String password, String name){
        Member member = new Member(loginId, password, name);

        return memberService.join(member);
    }





}
