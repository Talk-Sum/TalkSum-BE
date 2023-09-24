package talksum.talksum.service.SummaryService;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class GPTRequestAttrs {
    @JsonProperty("model")
    private String model;
    @JsonProperty("max_tokens")
    private int max_tokens;
    @JsonProperty("temperature")
    private double temperature;
    @JsonProperty("messages")
    private List<GPTRequestMessage> messages;
}
