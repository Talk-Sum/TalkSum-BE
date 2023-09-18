package talksum.talksum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talksum.talksum.domain.dto.NoteDto;
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

    private static final int PAGE_NOTE_COUNT=7; //한 페이지에 존재하는 노트 수

    /* convertEntityToDto */
    private NoteDto convertEntityToDto(Note note){
        return NoteDto.builder()
                .title(note.getTitle())
                .bookMark(note.isBookMark())
                .noteContent(note.getNoteContent())
                .author(note.getAuthor())
                .build();
    }

    @Transactional
    public List<NoteDto> getNoteList(Integer pageNum){
        Page<Note>page=noteRepository.findAll(PageRequest.of(pageNum-1, PAGE_NOTE_COUNT, Sort.by(Sort.Direction.ASC, "createdDate")));
        List<Note> noteEntities=page.getContent();
        List<NoteDto>noteDtoList=new ArrayList<>();

        for(Note note : noteEntities){
            noteDtoList.add(this.convertEntityToDto(note));
        }

        return noteDtoList;
    }

    /* For editing note, viewing noteDetail */
    @Transactional
    public Note /*NoteDto*/ getNote(Long noteId) throws Exception{
        Optional<Note> noteEntityWrapper=noteRepository.findById(noteId);
        if(noteEntityWrapper.isPresent()){
            Note note=noteEntityWrapper.get();

            // noteDto객체 만들어서 현재 노트 정보를 담아서 return?

            return note;
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
    public List<NoteDto> searchPosts(String keyword) {
        List<Note> noteEntities = noteRepository.findByNoteContentContaining(keyword);
        List<NoteDto> boardDtoList = new ArrayList<>();

        if (noteEntities.isEmpty()) return boardDtoList;

        for (Note note : noteEntities) {
            boardDtoList.add(this.convertEntityToDto(note));
        }

        return boardDtoList;
    }

    /* BookMark toggle
    @Transactional
    public Note bookMarkOperation(Long noteId,NoteDto noteDto){

    }*/

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
    }

}