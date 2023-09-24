package talksum.talksum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import talksum.talksum.service.STTservice.GoogleSTTService;

import java.io.IOException;

@SpringBootTest
public class GoogleSTTServiceTest {
    @Autowired
    GoogleSTTService googleSTTService;
    @Value("${AUDIO_FILE_PATH}")
    private String AUDIO_FILE_PATH;
    private String fileName = "audio_20230918194248302_a25b19e68ade43d7aff6c6fe65f60b8a.flac";
    @Test
    void googleSTTService() throws Exception {
        String script = googleSTTService.executeSTT("audio_20230918194248302_a25b19e68ade43d7aff6c6fe65f60b8a.flac", "flac", "en-US");
        System.out.println(script);
    }

    @Test
    void getSampleRateTest() throws IOException {
        int sampleRate = googleSTTService.getSampleRateFromFlac(AUDIO_FILE_PATH + fileName);
        System.out.println(sampleRate);
    }


}
