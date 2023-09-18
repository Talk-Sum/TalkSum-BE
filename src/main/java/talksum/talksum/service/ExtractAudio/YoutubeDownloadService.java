package talksum.talksum.service.ExtractAudio;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        ProcessBuilder pb = new ProcessBuilder(
                "yt-dlp",
                "-x",
                "--audio-format", "flac",
                "--audio-quality", "0:1",
                "--output", (AUDIO_FILE_PATH + fileName),
                url);

        try {
            Process process = pb.start();
            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            // 출력 읽기
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("exitCode: " + exitCode);

            reader.close();
            inputStreamReader.close();
            inputStream.close();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static String generateFilename() {
        // 현재 시간을 기반으로 고유한 파일 이름 생성
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = dateFormat.format(new Date());

        // 랜덤한 UUID 생성
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        // 파일 이름 형식 결정
        String uniqueFilename = "audio_" + timestamp + "_" + uuid;

        return uniqueFilename;
    }
}
