package talksum.talksum.service.ExtractAudio;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class YoutubeDownloadService implements ExtractAudioService{

    @Value("${AUDIO_FILE_PATH}")
    private String AUDIO_FILE_PATH;
    @Override
    public String getAudioName(String url) {
        String fileName = generateFilename();
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(
                "yt-dlp",
                "-x",
                "--audio-format", "wav",
                "--audio-quality", "0:1",
                "--output", (AUDIO_FILE_PATH + fileName),
                url
        );
        return fileName;
    }

    public static String generateFilename() {
        // 현재 시간을 기반으로 고유한 파일 이름 생성
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = dateFormat.format(new Date());

        // 랜덤한 UUID 생성
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        // 파일 이름 형식 결정
        String uniqueFilename = "audio_" + timestamp + "_" + uuid + ".wav";

        return uniqueFilename;
    }
}
