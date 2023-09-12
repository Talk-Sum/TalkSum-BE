package talksum.talksum.service.STTservice;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class GoogleSTTService implements STTservice{
    @Value("${stt.dirpath}")
    private String dirPath;

    @Override
    public String executeSTT(String fileName) throws Exception {
        String filePath = dirPath + fileName;
        String transcript = "";
        try (SpeechClient speechClient = SpeechClient.create()) {

            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);
            ByteString audioBytes = ByteString.copyFrom(data);

            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setLanguageCode("en-US")
                    .setAudioChannelCount(2) // AudioChannel 수 확인하기
                    .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();

            RecognizeResponse response = speechClient.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            for (SpeechRecognitionResult result : results) {
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                transcript += alternative.getTranscript();
            }
        }
        if(transcript == ""){
            return new String("음성이 인식되지 않았습니다.");
        }
        return transcript;
    }
}
