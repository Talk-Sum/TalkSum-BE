package talksum.talksum.service.SummaryService;

import lombok.Value;

@Value
public class GPTResponse {
    String id, object, model;
    int created;
    GPTResponseChoice[] choices;
    GPTResponseUsage usage;
}
