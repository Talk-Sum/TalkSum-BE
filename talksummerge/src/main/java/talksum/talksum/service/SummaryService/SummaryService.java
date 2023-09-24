package talksum.talksum.service.SummaryService;

import org.springframework.stereotype.Service;

import java.io.IOException;

public interface SummaryService {
    String executeSummary(String content) throws IOException, InterruptedException;
}
