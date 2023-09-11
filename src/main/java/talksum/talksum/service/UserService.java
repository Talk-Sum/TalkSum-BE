package talksum.talksum.service;

import org.springframework.stereotype.Service;
import talksum.talksum.repository.User;
import talksum.talksum.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long saveUser(User user){
        return userRepository.save(user).getUserId();
    }

}
