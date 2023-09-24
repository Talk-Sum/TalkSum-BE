package talksum.talksum.service.SummaryService;

import lombok.Value;

@Value
public class GPTRequest {
    private String model, prompt;
    private int temperature, max_tokens;
}
