package talksum.talksum.service.ExtractAudio;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
@Primary
@Service
public class YoutubeDownloadService{

    GenerateFileNameService generateFileNameService;

    public YoutubeDownloadService(GenerateFileNameService generateFileNameService) {
        this.generateFileNameService = generateFileNameService;
    }

    @Value("${AUDIO_FILE_PATH}")
    private String AUDIO_FILE_PATH;
    public String getAudioName(String url) {
        String fileName = generateFileNameService.generateFilename();
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
}
