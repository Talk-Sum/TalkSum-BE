package talksum.talksum.service.SummaryService;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GPTRequestMessage {
    private String role;
    private String content;

    public GPTRequestMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
