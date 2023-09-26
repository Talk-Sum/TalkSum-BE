package talksum.talksum.domain.dto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
public class ApiUploadForm {
    private String url;
    private MultipartFile audioFile;
    @NotBlank(message = "언어를 선택하세요.")
    private String language;
}