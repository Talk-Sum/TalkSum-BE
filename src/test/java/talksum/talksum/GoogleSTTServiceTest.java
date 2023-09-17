package talksum.talksum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import talksum.talksum.service.STTservice.GoogleSTTService;

@SpringBootTest
public class GoogleSTTServiceTest {
    @Autowired
    GoogleSTTService googleSTTService;
    @Test
    void googleSTTService() throws Exception {
        String script = googleSTTService.executeSTT("test.wav");
        System.out.println(script);
    }

}
