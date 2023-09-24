package talksum.talksum.service.SummaryService;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GPTResponseUsage {
    int prompt_tokens, completion_tokens, total_tokens;
}
