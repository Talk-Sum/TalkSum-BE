package talksum.talksum.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import talksum.talksum.domain.dto.ApiUploadForm;
import talksum.talksum.service.ExtractAudio.FileDownloadService;
import talksum.talksum.service.ExtractAudio.YoutubeDownloadService;
import talksum.talksum.service.STTservice.STTservice;
import talksum.talksum.service.SummaryService.SummaryService;

@RestController
public class RestTalksumController {
    private final STTservice sttService;
    private final FileDownloadService fileDownloadService;
    private final YoutubeDownloadService youtubeDownloadService;
    private final SummaryService summaryService;

    public RestTalksumController(@Qualifier("googleSTTService") STTservice sttService, FileDownloadService fileDownloadService, YoutubeDownloadService youtubeDownloadService, SummaryService summaryService) {
        this.sttService = sttService;
        this.fileDownloadService = fileDownloadService;
        this.youtubeDownloadService = youtubeDownloadService;
        this.summaryService = summaryService;
    }

    @PostMapping("/api/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestBody ApiUploadForm apiUploadForm){
        System.out.println("uploadFile 요청 성공");
        System.out.println("audioFile :" + apiUploadForm.getAudioFile());
        System.out.println("language :" + apiUploadForm.getLanguage());
        try {

            String originalFilename = apiUploadForm.getAudioFile().getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileName = fileDownloadService.getAudioName(apiUploadForm.getAudioFile(), fileExtension);
            String sttText = sttService.executeSTT(fileName, "flac", apiUploadForm.getLanguage());
            String summaryText = summaryService.executeSummary(sttText);
            System.out.println(summaryText);
            System.out.println("전송 완료");
            return ResponseEntity.ok(summaryText);

        }catch (Exception e) {
            return ResponseEntity.ok("업로드 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/api/uploadLink")
    public ResponseEntity<String> uploadLink(@RequestBody ApiUploadForm apiUploadForm) {
        System.out.println("uploadLink 요청 성공");
        System.out.println("Url :" + apiUploadForm.getUrl());
        System.out.println("language :" + apiUploadForm.getLanguage());

        try {
            System.out.println("apiupload youtube start");
            String fileName = youtubeDownloadService.getAudioName(apiUploadForm.getUrl()) + ".flac";
            String sttText = sttService.executeSTT(fileName, "flac", apiUploadForm.getLanguage());
            String summaryText = summaryService.executeSummary(sttText);
            System.out.println(summaryText);
            System.out.println("전송 완료");
            return ResponseEntity.ok(summaryText);

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("올바른 링크를 입력하세요.");
        }
    }
}
