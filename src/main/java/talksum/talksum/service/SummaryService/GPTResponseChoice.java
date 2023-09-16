package talksum.talksum.service.SummaryService;

import lombok.Value;

@Value
public class GPTResponseChoice {
    String text, finish_reason;
    int index;
    Object logprobs;
}
