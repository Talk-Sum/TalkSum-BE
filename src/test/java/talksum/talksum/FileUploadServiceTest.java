package talksum.talksum;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import talksum.talksum.service.STTservice.FileUploadService;

import java.io.IOException;

@SpringBootTest
public class FileUploadServiceTest {
    @Autowired
    FileUploadService fileUploadService;

    @Test
    void uploadAudioFileTest() throws IOException {
        String fileName = "audio_20230918194248302_a25b19e68ade43d7aff6c6fe65f60b8a.flac";
        fileUploadService.uploadAudioFile(fileName);

    }

}
