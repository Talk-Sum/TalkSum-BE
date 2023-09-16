package talksum.talksum.service.SummaryService;

public record GPTRequestAttrs() {

    static String Model;
    static int temperature, maxTokens;
}
