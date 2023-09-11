package talksum.talksum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import talksum.talksum.service.STTservice;

@RestController
public class TalksumController {

    private final STTservice sttservice;

    public TalksumController(STTservice sttservice) {
        this.sttservice = sttservice;
    }

    @PostMapping("/create")
    public ResponseEntity<> createNote(){

    }

}
