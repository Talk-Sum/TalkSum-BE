package talksum.talksum.service.SummaryService;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GPTResponseChoice {
    String finish_reason;
    int index;
    GPTResponseMessage message;
}
