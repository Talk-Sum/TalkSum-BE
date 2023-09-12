package talksum.talksum.service.SummaryService;

import lombok.Value;

@Value
public class GPTResponseUsage {
    int prompt_tokens, completion_tokens, total_tokens;
}
