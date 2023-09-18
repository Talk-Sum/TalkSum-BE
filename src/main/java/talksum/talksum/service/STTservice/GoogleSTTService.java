package talksum.talksum.service.STTservice;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GoogleSTTService implements STTservice{
    @Value("${GS_PATH}")
    private String GS_PATH;

    @Override
    public String executeSTT(String fileName, String fileExtension, String language) throws Exception {
        String filePath = GS_PATH + fileName;
        String transcript = "";
        int sampleRate = 16000;
        RecognitionConfig.AudioEncoding encoding = null;

        if(fileExtension == "WAV" || fileExtension == "wav"){
            encoding = RecognitionConfig.AudioEncoding.LINEAR16;
        }
        else if(fileExtension == "FLAC" || fileExtension == "flac"){
            encoding = RecognitionConfig.AudioEncoding.FLAC;
        }
        try (SpeechClient speechClient = SpeechClient.create()) {
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setLanguageCode(language)
                            .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setUri(filePath)
                    .build();

            if(fileExtension == "WAV" || fileExtension == "wav"){
                config.toBuilder().setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                        .setSampleRateHertz(getSampleRateFromFlac(filePath));
            }
            else if(fileExtension == "FLAC" || fileExtension == "flac"){
                encoding = RecognitionConfig.AudioEncoding.FLAC;
            }


            // STT 호출
            RecognizeResponse response = speechClient.recognize(config, audio);
            assert(response == null);
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
    public int getSampleRateFromFlac(String flacFilePath) throws IOException {

        // sox 명령을 사용하여 샘플 레이트 확인
        Process process = Runtime.getRuntime().exec("sox --info -r " + flacFilePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        int sampleRate = 16000; // 샘플 레이트 초기값 설정

        while ((line = reader.readLine()) != null) {
            // 라인에서 샘플 레이트 정보를 추출
            if (line.startsWith("Sample Rate")) {
                String[] parts = line.split(":");
                if (parts.length >= 2) {
                    try {
                        sampleRate = Integer.parseInt(parts[1].trim());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // 프로세스 종료 및 자원 해제
        process.destroy();;
        return sampleRate;
    }
}
