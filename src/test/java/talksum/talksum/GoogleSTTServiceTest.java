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
        String script = googleSTTService.executeSTT("audio_20230918045753940_c1e54b000c5043b99a58a91fafcc541d.flac");
        System.out.println(script);
    }

}
