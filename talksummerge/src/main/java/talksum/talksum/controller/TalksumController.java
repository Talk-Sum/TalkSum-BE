package talksum.talksum.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import talksum.talksum.domain.dto.*;
import lombok.extern.slf4j.Slf4j;
import talksum.talksum.domain.entity.Member;
import talksum.talksum.service.ExtractAudio.FileDownloadService;
import talksum.talksum.service.ExtractAudio.YoutubeDownloadService;
import talksum.talksum.service.NoteService;
import talksum.talksum.service.STTservice.STTservice;
import talksum.talksum.service.MemberService;
import talksum.talksum.service.SummaryService.SummaryService;

import javax.naming.AuthenticationException;
import java.util.List;

@Slf4j
@Controller
public class TalksumController {

    private final STTservice sttservice;
    private final SummaryService summaryService;
    private final MemberService memberService;
    private final NoteService noteService;

    private FileDownloadService fileDownloadService;
    private YoutubeDownloadService youtubeDownloadService;

    public TalksumController(@Qualifier("googleSTTService") STTservice sttservice, MemberService memberService, NoteService noteService, FileDownloadService fileDownloadService, YoutubeDownloadService youtubeDownloadService, SummaryService summaryService) {
        this.sttservice = sttservice;
        this.memberService = memberService;
        this.noteService = noteService;
        this.fileDownloadService = fileDownloadService;
        this.youtubeDownloadService = youtubeDownloadService;
        this.summaryService = summaryService;
    }

    /* Main page */
    @GetMapping("/")
    public String mainPage(){
        return "Main";
    }

    /* 회원 가입 폼 */
    @GetMapping("/signup")
    public String createUserForm(Model model){
        model.addAttribute("MemberForm",new MemberSaveForm());
        return "members/signup";
    }

    /* 회원가입
    @PostMapping("/signup")
    public String createUser(@Validated @ModelAttribute ("MemberForm")MemberSaveForm memberSaveForm, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "members/signup";
        }

        memberService.join(memberSaveForm);

        log.info("member = {}",memberSaveForm.toString());

        return "redirect:/";
    }*/

    /* 회원가입 */
    @PostMapping("/signup")
    public String createUser(@Validated @ModelAttribute("MemberForm") MemberSaveForm memberSaveForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "members/signup";
        }

        // ID 중복 검사
        if(memberService.existsByLoginId(memberSaveForm.getLoginId())){
            redirectAttributes.addFlashAttribute("errorMessage", "이미 가입된 아이디입니다.");
            return "redirect:/signup";
        }

        // 이름 중복 검사
        if(memberService.existsByName(memberSaveForm.getName())){
            redirectAttributes.addFlashAttribute("errorMessage", "이미 존재하는 닉네임입니다.");
            return "redirect:/signup";
        }

        memberService.join(memberSaveForm);
        log.info("member = {}", memberSaveForm.toString());

        return "redirect:/";
    }


    @GetMapping("/member/{memberId}")
    public String myPage(@SessionAttribute(name = "loginMember", required = true)Member loginMember, @PathVariable Long memberId, Model model){

        if(!(memberId.equals(loginMember.getMemberId()))){
            return "redirect:/login";
        }

        Member member=memberService.findMemberById(memberId);
        model.addAttribute("member",member);

        // redirect할 페이지
        return "mypages/mypage";
    }

    /* 회원 정보 수정 페이지 */
    @GetMapping("/member/{memberId}/edit")
    public String updateMemberForm(@SessionAttribute(name="loginMember", required = true)Member loginMember, @PathVariable Long memberId, Model model){

        if(!loginMember.getMemberId().equals(memberId)){
            return "redirect:mypages/mypage";
        }

        Member member=memberService.findMemberById(memberId);
        model.addAttribute("member",member);

        return "members/updateMemberForm";
    }

    /* 회원 정보 수정 처리 */
    @PutMapping("/member/{memberId}/edit")
    public String updateMember(@PathVariable Long memberId, @Validated @ModelAttribute("memberParam")MemberUpdateForm memberUpdateForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "members/updateMemberForm";
        }

        memberService.updateMember(memberId, memberUpdateForm);

        return "redirect:/member/{memberId}";
    }

    /* 회원 정보 삭제 */
    @DeleteMapping("/member/delete/{memberId}")
    public String delete(@SessionAttribute(name="loginMember", required = true)Member loginMember, @PathVariable Long memberId, HttpSession session){

        if(!(memberId.equals(loginMember.getMemberId()))){
            return "redirect:mypages/mypage";
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
        model.addAttribute("loginForm", new LoginForm());
        return "members/login";
    }

    /* 로그인 처리 */
    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request, @RequestParam(defaultValue = "/")String redirectURL,RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "members/login";
        }
        try{
            Member loginMember = memberService.login(loginForm.getLoginId(), loginForm.getPassword());
            if(loginMember==null){
                bindingResult.reject("loginFail","아이디 혹은 비밀번호를 입력해주세요.");
                return "members/login";
            }
            HttpSession session=request.getSession(true);
            session.setAttribute("loginForm",loginMember);
            log.info("Current User is {}", session.getAttribute("loginForm"));
            return "redirect:" + redirectURL;
        }
        catch (AuthenticationException e){
            redirectAttributes.addFlashAttribute("errorMessage",e.getMessage());
            return "redirect:/login";
        }
    }

    /* 로그아웃 */
    @GetMapping("/logout")

    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if(session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }


    /* 즐겨찾기 토글 */
    @GetMapping("/toggleBookMark/{noteId}")
    public String toggleBookMark(@PathVariable Long noteId){
        noteService.toggleNoteBookMark(noteId);
        return "redirect:/mypage";
    }

    /* 노트 생성 */
    @PostMapping("/upload")
    public String upload(@Validated @ModelAttribute("uploadForm") UploadForm uploadForm, @SessionAttribute(name = "loginForm") Member member, BindingResult bindingResult, Model model, HttpSession httpSession){
        System.out.println(member.getLoginId());
        if(bindingResult.hasErrors()){
            for (ObjectError allError : bindingResult.getAllErrors()) {
                System.out.println(allError);
            }
            return "/mypages/mypage";
        }
        if(uploadForm.getAudioFile() == null && uploadForm.getUrl() == null){
            model.addAttribute("errorMessage", "오디오 파일 혹은 URL을 추가해주세요.");
            return "/mypages/mypage";
        }
        long startTime = System.currentTimeMillis();

        //youtube url로 추출
        model.addAttribute("uploadForm", new UploadForm());

        if(!uploadForm.getUrl().isEmpty()) {
            try {
                System.out.println("upload youtube start");
                String fileName = youtubeDownloadService.getAudioName(uploadForm.getUrl()) + ".flac";
                long curTime = System.currentTimeMillis();
                System.out.println("finish download file : " + (curTime - startTime));
                startTime = curTime;

                //model.addAttribute 로 바로 노트화?
                String sttText = sttservice.executeSTT(fileName, "flac", uploadForm.getLanguage());
                System.out.println("finish executeSTT : " + (curTime - startTime));
                startTime = curTime;
                String summaryText = summaryService.executeSummary(sttText);
                System.out.println("finish summary : " + (curTime - startTime));
                System.out.println(summaryText);
                NoteDto noteDto = NoteDto.builder()
                        .title(uploadForm.getTitle())
                        .bookMark(false)
                        .isTrash(false)
                        .author(member)
                        .audioText(sttText)
                        .noteContent(summaryText)
                        .build();
                noteService.saveNote(noteDto);
                return "/mypages/mypage";
            } catch (Exception e) {
                model.addAttribute("errorMessage", "올바른 URL을 입력하였는지 확인해주세요.");
                return "/mypages/mypage";
            }
        }
        // file로 추출
        else{
            try {
                System.out.println("upload file start");
                String originalFilename = uploadForm.getAudioFile().getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                String fileName = fileDownloadService.getAudioName(uploadForm.getAudioFile(), fileExtension);
                long curTime = System.currentTimeMillis();
                System.out.println("finish download file : " + (curTime - startTime));
                startTime = curTime;
                String sttText = sttservice.executeSTT(fileName, fileExtension, uploadForm.getLanguage());
                System.out.println("finish executeSTT : " + (curTime - startTime));
                startTime = curTime;
                String summaryText = summaryService.executeSummary(sttText);
                System.out.println("finish summary : " + (curTime - startTime));
                System.out.println(summaryText);
                NoteDto noteDto = NoteDto.builder()
                        .title(uploadForm.getTitle())
                        .bookMark(false)
                        .isTrash(false)
                        .author(member)
                        .audioText(sttText)
                        .noteContent(summaryText)
                        .build();
                noteService.saveNote(noteDto);
                return "/mypages/mypage";
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "영상 처리 도중 오류가 발생하였습니다.");
                return "/mypages/mypage";
            }
        }
        //youtube URL
    }

    /* 노트 목록(마이페이지) 휴지통으로 보내지 않은 노트들만 보이게! */
    @GetMapping("/mypage")
    public String mypage(Model model, @RequestParam(value = "page", defaultValue = "1")Integer pageNum, @SessionAttribute(name = "loginForm") Member member, HttpServletRequest request){
        List<NoteDto> noteDtoList=noteService.getNoteList(member, pageNum);
        Integer[] pageList=noteService.getPageList(pageNum, (long)noteDtoList.size());
        model.addAttribute("uploadForm", new UploadForm());


        if (model.containsAttribute("message")) {
            String message = (String) model.asMap().get("message");
            model.addAttribute("message", message);
        }
        model.addAttribute("noteDtoList", noteDtoList);
        model.addAttribute("pageList", pageList);
        HttpSession session = request.getSession(true);
        session.setAttribute("loginForm", member);

        return "mypages/mypage";
    }

    /* 노트 목록(마이페이지) 휴지통으로 보내지 않은 노트들만 보이게! */
    @GetMapping("/mypageReload")
    public String mypage(@SessionAttribute("loginForm")Member member, Model model, @RequestParam(value = "page", defaultValue = "1")Integer pageNum, HttpServletRequest request){
        System.out.println("GET to mypageReload");
        HttpSession session = request.getSession(true);
        List<NoteDto> noteDtoList=noteService.getNoteList(member, pageNum);
        Integer[] pageList=noteService.getPageList(pageNum,  (long)noteDtoList.size());
        session.setAttribute("loginForm", member);
        model.addAttribute("pageList", pageList);
        model.addAttribute("noteDtoList", noteDtoList);
        return "mypages/All";
    }


    /* 즐겨찾기 추가된 목록 보이기 */
    @GetMapping("/bookMark")
    public String moveImportantPage(@SessionAttribute("loginForm")Member member, Model model, @RequestParam(value = "page", defaultValue = "1")Integer pageNum, HttpServletRequest request){
        System.out.println("GET 전송 성공");
        List<NoteDto> noteDtoList = noteService.getUserBookMarkNote(member, pageNum);
        Integer[] pageList=noteService.getPageList(pageNum, (long)noteDtoList.size());
        HttpSession session = request.getSession(true);
        session.setAttribute("loginForm", member);


        model.addAttribute("noteDtoList", noteDtoList);
        model.addAttribute("pageList", pageList);

        return "mypages/Important";
    }

    /* 휴지통으로 노트 이동시키기 */
    @PostMapping("/trash")
    public String moveNoteToTrash(@RequestParam("noteIds")List<Long> noteIds, RedirectAttributes redirectAttributes){

        if (noteIds == null || noteIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "노트를 선택해주세요.");
            return "redirect:/mypage";
        }

        noteService.moveNoteToTrash(noteIds);
        redirectAttributes.addFlashAttribute("message", "노트가 휴지통으로 이동되었습니다.");

        return "redirect:/mypage";
    }


    /* 휴지통 목록 띄우기 */
    @GetMapping("/myTrashList")
    public String viewMyTrashNotes(@SessionAttribute("loginForm")Member member, Model model, @RequestParam(value = "page", defaultValue = "1")Integer pageNum, HttpServletRequest request){
        HttpSession session = request.getSession(true);
        session.setAttribute("loginForm",member);

        List<NoteDto> trashNotes=noteService.getNotesInTrash(member, pageNum); /* 한 페이지의 최대 노트가 7개라 휴지통 노트의 개수가 7개만 들어간다. */
        Integer[] pageList=noteService.getPageList(pageNum, (long)trashNotes.size());

        log.info("Number of trash notes: " + trashNotes);
        model.addAttribute("noteDtoList", trashNotes);
        model.addAttribute("pageList",pageList);
        log.info("This is TrashNoteList");
        System.out.println("This is TrashNoteList");
        return "/mypages/Trash";
    }

    /* 노트 검색 */
    @GetMapping("/note/search")
    public String search(@SessionAttribute(name = "loginForm", required = true) Member member,@RequestParam(value = "keyword")String keyword, Model model, HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1")Integer pageNum){
        HttpSession session = request.getSession(true);
        session.setAttribute("loginForm",member);

        List<NoteDto> noteDtoList= noteService.searchPosts(member, keyword, pageNum);
        Integer[] pageList=noteService.getPageList(pageNum, (long)noteDtoList.size());

        log.info("Searching....");

        model.addAttribute("pageList", pageList);
        model.addAttribute("noteDtoList", noteDtoList);
        model.addAttribute("uploadForm", new UploadForm());
        return "mypages/mypage";
    }

    /* 노트 삭제 처리 */
    @DeleteMapping("/mypage/note/{noteId}")
    public String delete(NoteDto noteDto, @PathVariable("noteId")Long noteId, @SessionAttribute(name = "loginForm", required = true) Member member){
        noteDto.setAuthor(member);
        if(!noteDto.getAuthor().equals(member)){
            return "redirect:mypages/NoteDetail";
        }
        noteService.deleteNote(noteDto.toEntity().getNoteId());
        return "redirect:/mypage";
    }

    /* 노트 내용 보이기 */
    @GetMapping("/page")
    public String viewPage(@RequestParam("noteId") Long noteId, Model model) throws Exception {
        NoteDto noteDto = noteService.getNote(noteId);
        String audioText = noteDto.getAudioText();
        String noteContent = noteDto.getNoteContent();
        model.addAttribute("audioText", audioText);
        model.addAttribute("noteContent", noteContent);
        return "/mypages/Page";
    }

    /* 휴지통 비우기 */
    @GetMapping("/emptyTrash")
    public String emptyTrash(@SessionAttribute(name = "loginForm", required = true) Member loginMember) {

        noteService.emptyTrash(loginMember);
        System.out.println("Successfully clear TrashBin!");
        log.info("Successfully clear TrashBin!");
        return "/mypages/Trash";
    }
}
