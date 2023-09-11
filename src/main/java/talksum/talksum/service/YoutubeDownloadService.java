package talksum.talksum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YoutubeDownloadService {
    private String url;
    private final String command = "yt-dlp -x --audio-format wav ";
    private final ExecuteCommandService executeCommandService;

    public YoutubeDownloadService(ExecuteCommandService executeCommandService) {
        this.executeCommandService = executeCommandService;
    }
}
