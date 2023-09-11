package talksum.talksum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import talksum.talksum.repository.User;
import talksum.talksum.repository.UserRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE) // web환경 사용 X
@Transactional
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void saveTest(){
        User user = new User();
        user.setName("junha");
        user.setLoginId("junhaa");
        user.setPassword("1234");

        userRepository.save(user);

        assertThat(userRepository.count()).isEqualTo(1);
    }


}
