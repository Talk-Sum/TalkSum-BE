package talksum.talksum;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import talksum.talksum.service.ExtractAudio.YoutubeDownloadService;

@SpringBootTest
public class ExtractAudioServiceTest {
    @Autowired
    YoutubeDownloadService youtubeDownloadService;

    @Test
    void youtubeDownloadTest(){
        String url = "https://www.youtube.com/watch?v=xx-srlDfdYQ";
        String audioname = youtubeDownloadService.getAudioName(url);

    }

}
