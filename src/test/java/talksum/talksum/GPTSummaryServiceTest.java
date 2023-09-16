package talksum.talksum;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import talksum.talksum.service.SummaryService.GPTSummaryService;

@SpringBootTest
public class GPTSummaryServiceTest {
    @Test
    void GPTSummaryTest(){
        String script;
        GPTSummaryService gptSummaryService = new GPTSummaryService();
        gptSummaryService.executeSummary(script);

    }

}
