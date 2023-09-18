package talksum.talksum.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import talksum.talksum.domain.dto.MemberDto;
import lombok.extern.slf4j.Slf4j;
import talksum.talksum.domain.dto.NoteDto;
import talksum.talksum.domain.entity.Member;
import talksum.talksum.repository.NoteRepository;
import talksum.talksum.service.ExtractAudio.FileDownloadService;
import talksum.talksum.service.ExtractAudio.GenerateFileNameService;
import talksum.talksum.service.ExtractAudio.YoutubeDownloadService;
import talksum.talksum.service.NoteService;
import talksum.talksum.service.STTservice.GoogleSTTService;
import talksum.talksum.service.STTservice.STTservice;
import talksum.talksum.service.MemberService;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class TalksumController {

    private final STTservice sttservice;
    private final MemberService memberService;
    private final NoteService noteService;

    private FileDownloadService fileDownloadService;
    private YoutubeDownloadService youtubeDownloadService;
    private NoteRepository noteRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public TalksumController(@Qualifier("googleSTTService") STTservice sttservice, MemberService memberService, NoteService noteService, FileDownloadService fileDownloadService, YoutubeDownloadService youtubeDownloadService) {
        this.sttservice = sttservice;
        this.memberService = memberService;
        this.noteService = noteService;
        this.fileDownloadService = fileDownloadService;
        this.youtubeDownloadService = youtubeDownloadService;
    }

    /* Main page */
    @GetMapping("/")
    public String mainPage(){
        return "index";
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

    /* 로그아웃 */
    @PostMapping("/logout")

    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if(session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }

    /* 요약노트 생성 페이지 */
    @GetMapping("/note")
    public String note(){
        return "mypages/createNote";
    }

    /* 유튜브 링크로 입력 -> 텍스트 추출 후 view 화면에 text 를 노트로 띄우기 */
    @PostMapping("/uploadLink")
    public ModelAndView uploadLink(@RequestParam("inputUrl") String video, String language){
        // yt-dlp 사용해서 음성데이터 받아오기
        //예외처리 .. 도와주세요
        String fileName = youtubeDownloadService.getAudioName(video);

        try {
            //model.addAttribute 로 바로 노트화?
            String text = sttservice.executeSTT(fileName, "flac", language);
            //noteRepository.save(text);

            ModelAndView modelAndView= new ModelAndView("mypages/mypage");
            modelAndView.addObject("message", "영상 처리 및 텍스트 추출이 완료되었습니다.");
            return modelAndView;
        }
        catch(Exception e){
            ModelAndView errorModelAndView = new ModelAndView("mypages/fileUploadPage");
            errorModelAndView.addObject("errorMessage", "영상 처리 도중 오류가 발생하였습니다.");
            return errorModelAndView;
        }
    }

    /* 음성 데이터로 입력 -> 텍스트 추출 후 view 화면에 text 를 노트로 띄우기 */
    @PostMapping("/uploadFile")
    public ModelAndView uploadFile(@RequestParam("inputFile") MultipartFile audioFile, String language){
        try {
            String originalFilename = audioFile.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1); // 파일 확장자

            String fileName = fileDownloadService.getAudioName(audioFile, fileExtension);
            String text = sttservice.executeSTT(fileName, fileExtension, language);
            //model.addAttribute 로 바로 노트화?
            //noteRepository.saveNote(text);

            ModelAndView modelAndView= new ModelAndView("mypages/mypage");
            modelAndView.addObject("message", "영상 처리 및 텍스트 추출이 완료되었습니다.");
            return modelAndView;
        }
        catch(Exception e){
            ModelAndView errorModelAndView = new ModelAndView("mypages/fileUploadPage");
            errorModelAndView.addObject("errorMessage", "영상 처리 도중 오류가 발생하였습니다.");
            return errorModelAndView;
        }
    }

    /* 노트 목록(마이페이지) */
    @GetMapping("/mypage")
    public String mypage(Model model, @RequestParam(value = "page", defaultValue = "1")Integer pageNum){
        List<NoteDto> noteDtoList=noteService.getNoteList(pageNum);
        Integer[] pageList=noteService.getPageList(pageNum);

        model.addAttribute("noteDtoList", noteDtoList);
        model.addAttribute("pageList", pageList);

        return "mypages/mypage";
    }

    /* 노트 검색 */
    @GetMapping("/note/search")
    public String search(@RequestParam(value = "keyword")String keyword, Model model){
        List<NoteDto> noteDtoList= noteService.searchPosts(keyword);
        model.addAttribute("noteList", noteDtoList);

        return "mypages/mypage";
    }

    /* 노트 삭제 처리 */
    @DeleteMapping("/mypage/note/{noteId}")
    public String delete(NoteDto noteDto, @PathVariable("noteId")Long noteId, @SessionAttribute(name = "loginMember", required = true) Member loginMember){
        noteDto.setAuthor(loginMember);
        if(!noteDto.getAuthor().equals(loginMember)){
            return "redirect:mypages/NoteDetail";
        }
        noteService.deleteNote(noteDto.toEntity().getNoteId());
        return "redirect:/mypage";
    }


}
