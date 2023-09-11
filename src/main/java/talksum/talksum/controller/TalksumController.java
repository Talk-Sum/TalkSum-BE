package talksum.talksum.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import talksum.talksum.repository.User;
import talksum.talksum.service.STTservice;
import talksum.talksum.service.UserService;

@RestController
public class TalksumController {

    private final STTservice sttservice;
    private final UserService userService;
    public TalksumController(STTservice sttservice, UserService userService) {
        this.sttservice = sttservice;
        this.userService = userService;
    }
    @GetMapping("/createUser")
    public Long createUser(String loginId, String password, String name){
        User user = new User();
        user.setLoginId(loginId);
        user.setPassword(password);
        user.setName(name);

        return userService.saveUser(user);
    }


}
