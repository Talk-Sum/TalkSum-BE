package talksum.talksum.service.ExtractAudio;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileDownloadService {
    GenerateFileNameService generateFileNameService;

    public FileDownloadService(GenerateFileNameService generateFileNameService) {
        this.generateFileNameService = generateFileNameService;
    }

    @Value("${AUDIO_FILE_PATH}")
    private String AUDIO_FILE_PATH;

    public String getAudioName(MultipartFile file, String fileExtension) throws IOException{
        // 파일이 빈 경우
        if(file.isEmpty()){
            throw new NullPointerException("파일이 존재하지 않습니다.");
        }
        String fileName = generateFileNameService.generateFilename() + "." + fileExtension;

        // 로컬에 파일 저장
        File dest = new File(AUDIO_FILE_PATH + fileName);
        file.transferTo(dest);
        return fileName;
    }
}
