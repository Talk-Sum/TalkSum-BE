package talksum.talksum.service.STTservice;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {
    @Value("${BUCKET_NAME}")
    private String BUCKET_NAME;
    @Value("${AUDIO_FILE_PATH}")
    private String AUDIO_FILE_PATH;

    private final Storage storage;
    @Autowired
    public FileUploadService(){
        storage = StorageOptions.getDefaultInstance().getService();
    }

    public void uploadAudioFile(String fileName) throws IOException {
        // 로컬 파일을 읽어와 GCS 버킷에 업로드
        Path localPath = Paths.get(AUDIO_FILE_PATH + fileName);
        byte[] fileBytes = Files.readAllBytes(localPath);

        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, fileBytes);

    }
}
