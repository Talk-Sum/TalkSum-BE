package talksum.talksum.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import talksum.talksum.domain.dto.MemberDto;
import lombok.extern.slf4j.Slf4j;
import talksum.talksum.domain.entity.Member;
import talksum.talksum.service.STTservice.STTservice;
import talksum.talksum.service.MemberService;

@Slf4j
@RestController
public class TalksumController {
    private final STTservice sttservice;
    private final MemberService memberService;
    public TalksumController(@Qualifier("googleSTTService") STTservice sttservice, MemberService memberService) {
        this.sttservice = sttservice;
        this.memberService = memberService;
    }



    /* 회원 가입 폼 */
    @GetMapping("/member/signup")
    public String createUserForm(){
        return "members/signup";
    }

    /* 회원가입 */
    @PostMapping("/member/createUser")
    public String createUser(@Validated MemberDto memberDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "members/signup";
        }

        memberService.join(memberDto);

        log.info("member = {}",memberDto.toString());

        return "redirect:/";
    }

    @GetMapping("/member/{memberId}")
    public String myPage(@SessionAttribute(name = "loginMember", required = true)Member loginMember, @PathVariable Long memberId, Model model){

        if(!(memberId.equals(loginMember.getMemberId()))){
            return "redirect:/member/signup";
        }

        Member member=memberService.findMemberById(memberId);
        model.addAttribute("member",member);

        // redirect할 페이지
        return "members/mypage";
    }

    /* 회원 정보 수정 페이지 */
    @GetMapping("/member/{memberId}/edit")
    public String updateMemberForm(@SessionAttribute(name="loginMember", required = true)Member loginMember, @PathVariable Long memberId, Model model){

        if(!loginMember.getMemberId().equals(memberId)){
            return "redirect:/mypage";
        }

        Member member=memberService.findMemberById(memberId);
        model.addAttribute("member",member);

        return "members/updateMemberForm";
    }

    /* 회원 정보 수정 처리 */
    @PutMapping("/member/{memberId}/edit")
    public String updateMember(@PathVariable Long memberId, @Validated @ModelAttribute("memberParam") MemberDto editMemberParam, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "members/updateMemberForm";
        }

        memberService.updateMember(memberId, editMemberParam);

        return "redirect:/member/{memberId}";
    }

    /* 회원 정보 삭제 */
    @DeleteMapping("/member/delete/{memberId}")
    public String delete(@SessionAttribute(name="loginMember", required = true)Member loginMember, @PathVariable Long memberId, HttpSession session){

        if(!(memberId.equals(loginMember.getMemberId()))){
            return "redirect:/mypage";
        }

        Member member= memberService.findMemberById(memberId);
        memberService.withdrawal(member.getMemberId());

        if(session!=null){
            session.invalidate();
        }

        return "redirect:/";
    }

    /* 로그인 페이지 */
    @GetMapping("/login")
    public String loginForm(Model model){
        //model.addAttribute("loginForm", new MemberDto());
        return "login/loginForm";
    }

    /* 로그인 처리 */
    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("MemberDto") MemberDto form, BindingResult bindingResult, HttpServletRequest request, @RequestParam(defaultValue = "/")String redirectURL){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member loginMember = memberService.login(form.getLoginId(), form.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }
        HttpSession session = request.getSession(true);

        session.setAttribute("loginMember", loginMember);


        return "redirect:" + redirectURL;
    }


    /*
    @PostMapping("/createNote")
    public String createNote(@RequestParam("file") MultipartFile file, @RequestParam("url") String url, @RequestParam("title") String title, RedirectAttributes redirectAttributes){
        if(file != null && url == null){
        }
        else if(file == null && url != null){

        }
        else{
            redirectAttributes.addFlashAttribute("파일 업로드 혹은 유튜브 URL중 한 가지를 입력해주세요.");
            return "redirect:/error";
        }

    }

     */

    @PostMapping("/logout")

    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if(session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }
}
