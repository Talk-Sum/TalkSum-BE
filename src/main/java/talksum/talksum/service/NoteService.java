package talksum.talksum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talksum.talksum.domain.dto.NoteDto;
import talksum.talksum.domain.entity.Member;
import talksum.talksum.domain.entity.Note;
import talksum.talksum.repository.NoteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    private NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    private static final int BLOCK_PAGE_NUM_COUNT=5; //블럭 내에 존재할 수 있는 페이지 번호 수

    private static final int PAGE_NOTE_COUNT=4; //한 페이지에 존재하는 노트 수


    /* 노트 목록 불러오기_ 휴지통으로 이동하지 않은 노트들만 보여야 한다! */
    @Transactional
    public List<NoteDto> getNoteList(Member member, Integer pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, PAGE_NOTE_COUNT, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Note> page = noteRepository.findByAuthorAndIsTrashFalse(member, pageable);
        List<Note> noteEntities = page.getContent();
        List<NoteDto> noteDtoList = new ArrayList<>();

        for (Note note : noteEntities) {
            noteDtoList.add(note.toDTO());
        }

        return noteDtoList;
    }


    @Transactional
    public Long getNoteListCount(Member member){
        return noteRepository.countByAuthorAndIsTrashFalse(member);
    }


    /* 노트 수정 및 상세보기에 필요한 로직 */
    @Transactional
    public NoteDto getNote(Long noteId) throws Exception{
        Optional<Note> noteEntityWrapper=noteRepository.findById(noteId);
        if(noteEntityWrapper.isPresent()){
            Note note=noteEntityWrapper.get();
            return note.toDTO();
        }
        else{
            throw new Exception("해당 노트를 찾지 못했습니다.");
        }
    }

    /* save */
    @Transactional
    public Long saveNote(NoteDto noteDto) {
        return noteRepository.save(noteDto.toEntity()).getNoteId();
    }

    /* delete */
    @Transactional
    public void deleteNote(Long noteId){
        noteRepository.deleteById(noteId);
    }

    /* Search */
    @Transactional
    public List<NoteDto> searchPosts(Member member, String keyword, Integer pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, PAGE_NOTE_COUNT, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Note> noteEntities = noteRepository.findByAuthorAndTitleContainingAndIsTrashFalse(member, keyword, pageable);
        List<NoteDto> boardDtoList = new ArrayList<>();

        if (noteEntities.isEmpty()) return boardDtoList;

        for (Note note : noteEntities) {
            boardDtoList.add(note.toDTO());
        }

        return boardDtoList;
    }

    @Transactional
    public Long SearchPostsCount(Member member, String keyword){
        return noteRepository.countByAuthorAndTitleContainingAndIsTrashFalse(member, keyword);
    }

/*
    public Integer[] getPageList(Integer curPageNum) {

        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];
        Double postsTotalCount = Double.valueOf(noteRepository.count());
        Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_NOTE_COUNT)));

        Integer blockLastPageNum =
                (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT) ? curPageNum + BLOCK_PAGE_NUM_COUNT
                        : totalLastPageNum;

        curPageNum = (curPageNum <= 3) ? 1 : curPageNum - 2;

        for (int val = curPageNum, idx = 0; val <= blockLastPageNum; val++, idx++) {
            pageList[idx] = val;
        }

        return pageList;
    }*/

    /* GPT ver.*/
    public Integer[] getPageList(Integer curPageNum, Long listSize) {

        // 게시물의 총 수가 0일 경우 빈 배열을 반환합니다.
        Double postsTotalCount = Double.valueOf(listSize);
        if (postsTotalCount == 0) {
            return new Integer[0];
        }

        List<Integer> pageListTemp = new ArrayList<>();
        Integer totalLastPageNum = (int)Math.ceil(postsTotalCount / PAGE_NOTE_COUNT);

        Integer startPageNum = Math.max(1, curPageNum - (BLOCK_PAGE_NUM_COUNT / 2));
        Integer endPageNum = Math.min(totalLastPageNum, startPageNum + BLOCK_PAGE_NUM_COUNT - 1);

        // 블록의 시작 페이지 번호가 전체 페이지 수에 가까울 경우 시작 페이지 번호를 조정합니다.
        if (totalLastPageNum - startPageNum < BLOCK_PAGE_NUM_COUNT) {
            startPageNum = Math.max(1, totalLastPageNum - BLOCK_PAGE_NUM_COUNT + 1);
        }

        for (int val = startPageNum; val <= endPageNum; val++) {
            pageListTemp.add(val);
        }

        return pageListTemp.toArray(new Integer[0]);
    }


    /* 북마크된 노트 리스트 (휴지통 제외) */
    @Transactional
    public List<NoteDto> getUserBookMarkNote(Member member, int pageNum){
        Pageable pageable = PageRequest.of(pageNum - 1, PAGE_NOTE_COUNT, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Note> noteList = noteRepository.findByAuthorAndBookMarkTrueAndIsTrashFalse(member, pageable);
        List<NoteDto> noteDtoList = new ArrayList<>();
        for(Note n : noteList){
            noteDtoList.add(n.toDTO());
        }
        return noteDtoList;
    }

    @Transactional
    public Long getUserBookMarkNoteCount(Member member){
        return noteRepository.countByAuthorAndBookMarkTrueAndIsTrashFalse(member);
    }


    /* 휴지통으로 이동 처리 */
    @Transactional
    public void moveNoteToTrash(List<Long> noteIds){
        noteRepository.updateIsTrashStatus(noteIds, true);
    }

    /* 휴지통 목록 불러오기 */
    @Transactional
    public List<NoteDto> getNotesInTrash(Member member, Integer pageNum){
        Pageable pageable=PageRequest.of(pageNum-1, PAGE_NOTE_COUNT, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Note>page=noteRepository.findByAuthorAndIsTrashTrue(member, pageable);
        List<Note> trashNoteEntities=page.getContent();
        List<NoteDto> noteDtoList=new ArrayList<>();

        for(Note note: trashNoteEntities){
            noteDtoList.add(note.toDTO());
        }

        return noteDtoList;
    }

    @Transactional
    public Long getNotesInTrashCount(Member member){
        return noteRepository.countByAuthorAndIsTrashTrue(member);
    }


    /* 북마크 토글 */
    @Transactional
    public Long toggleNoteBookMark(Long noteId){
        NoteDto noteDto = noteRepository.findByNoteId(noteId).toDTO();
        noteDto.setBookMark(!noteDto.isBookMark());
        noteRepository.save(noteDto.toEntity());
        return noteId;
    }

    /* 휴지통 비우기 */
    @Transactional
    public void emptyTrash(Member loginMember) {
        noteRepository.deleteNotesByAuthorAndIsTrashTrue(loginMember);
    }

}