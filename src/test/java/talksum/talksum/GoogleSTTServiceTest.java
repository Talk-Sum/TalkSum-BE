package talksum.talksum;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import talksum.talksum.service.STTservice.GoogleSTTService;

@SpringBootTest
public class GoogleSTTServiceTest {
    @Test
    void googleSTTService() throws Exception {
        GoogleSTTService googleSTTService = new GoogleSTTService();
        String script = googleSTTService.executeSTT("test.wav");
        System.out.println(script);
    }

}
