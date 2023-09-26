package talksum.talksum.service.SummaryService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GPTSummaryService implements SummaryService {

    private static final String API_ENDPOINT = "https://api.openai.com/v1/chat/completions";

    private final RestTemplate restTemplate = new RestTemplate();


    @Value("${OPEN_AI_KEY}")
    private String OPEN_AI_KEY;
    private final String OPEN_AI_SETTING = "당신은 스크립트를 요약해야 합니다.\n제공하는 글을 대상으로 중요한 내용을 여러 문장으로 요약하세요.\n요구사항은 아래와 같습니다.\n1. 여러개의 요약 문장을 작성하세요.\n2. 요약된 문장은 한글로 작성되어야 합니다.\n3. 요약된 문장은 번호로 구별해주세요.\n4. 요약된 문장은 라인을 나눠 주세요.";

    @Override
    public String executeSummary(String content) throws IOException {
        HttpEntity<String> requestEntity = makeRequest(content);
        System.out.println(requestEntity.getBody());

        // POST 요청
        ResponseEntity<String> response = restTemplate.exchange(API_ENDPOINT, HttpMethod.POST, requestEntity, String.class);
        String summary = "";

        if(response.getStatusCode() == HttpStatus.OK){
            GPTResponse gptResponse = new ObjectMapper().readValue(response.getBody(), GPTResponse.class);
            String tmp = gptResponse.getChoices()[gptResponse.getChoices().length - 1].message.getContent();
            if(!tmp.isEmpty()){
                summary += tmp;
            }
        }
        else{
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
        }
        return summary;
    }

    // Request 객체 만들기
    public HttpEntity<String> makeRequest(String content) throws JsonProcessingException {
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + OPEN_AI_KEY);
        headers.set("Content-Type", "application/json");

        // 바디 매핑
        GPTRequestAttrs gptRequestAttrs = new GPTRequestAttrs();
        ObjectMapper objectMapper = new ObjectMapper();
        gptRequestAttrs.setModel("gpt-3.5-turbo-16k");
        gptRequestAttrs.setTemperature(0.2);
        gptRequestAttrs.setMax_tokens(10000);
        List<GPTRequestMessage> messages = new ArrayList<>();
        messages.add(new GPTRequestMessage("user", content));
        messages.add(new GPTRequestMessage("system", OPEN_AI_SETTING));
        //messages.add(new GPTRequestMessage("assistant", OPEN_AI_ASSISTANT_SETTING));
        gptRequestAttrs.setMessages(messages);

        String requestBody = objectMapper.writeValueAsString(gptRequestAttrs);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        return requestEntity;
    }

}
