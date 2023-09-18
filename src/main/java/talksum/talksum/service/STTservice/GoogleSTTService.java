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
    @Value("${GS_PATH}")
    private String GS_PATH;

    @Override
    public String executeSTT(String fileName) throws Exception {


        String filePath = GS_PATH + fileName;
        String transcript = "";

        try (SpeechClient speechClient = SpeechClient.create()) {
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setSampleRateHertz(16000)
                            .setLanguageCode("en-US")
                            .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setUri(audioUri)
                    .build();

            // STT 호출
            RecognizeResponse response = speechClient.recognize(config, audio);

            // STT 결과 출력
            List<SpeechRecognitionResult> results = response.getResultsList();
            for (SpeechRecognitionResult result : results) {
                transcript += result.getAlternatives(0).getTranscript();
            }
            return transcript;
        }
        /*

        try (SpeechClient speechClient = SpeechClient.create()) {

            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);
            ByteString audioBytes = ByteString.copyFrom(data);

            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                    .setLanguageCode("en-US")
                    //.setAudioChannelCount(2) // AudioChannel 수 확인하기
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

         */
    }
}
