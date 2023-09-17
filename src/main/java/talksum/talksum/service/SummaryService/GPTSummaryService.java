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

    /*
    public String executeSummary(String prompt) {
        String url = "https://api.openai.com/v1/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPEN_AI_KEY);

        Map<String, Object> requestBody = new HashMap<>();
        // 요청 질문
        requestBody.put("prompt", prompt);

        // 요청에 사용될 모델 설정
        requestBody.put("model", "text-davinci-003");

        // 완료시 생성할 최대 토큰수
        requestBody.put("max_tokens", 1500);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new OpenAIException("OpenAI API 호출 중 오류가 발생하였습니다.", e);
        }
    }
    */


    @Value("${OPEN_AI_KEY}")
    private String OPEN_AI_KEY;
    @Value("${OPEN_AI_SETTING}")
    private String OPEN_AI_SETTING;

    @Override
    public String executeSummary(String content) throws IOException {
        HttpEntity<String> requestEntity = makeRequest(content);

        // POST 요청
        ResponseEntity<String> response = restTemplate.exchange(API_ENDPOINT, HttpMethod.POST, requestEntity, String.class);
        String summary = "";

        if(response.getStatusCode() == HttpStatus.OK){
            GPTResponse gptResponse = new ObjectMapper().readValue(response.getBody(), GPTResponse.class);
            String tmp = gptResponse.getChoices()[gptResponse.getChoices().length - 1].message.getContent();
            if(!tmp.isEmpty()){
                summary += tmp.replace("\n", "").trim();
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
        gptRequestAttrs.setModel("gpt-3.5-turbo");
        gptRequestAttrs.setTemperature(0.2);
        gptRequestAttrs.setMax_tokens(2000);
        List<GPTRequestMessage> messages = new ArrayList<>();
        messages.add(new GPTRequestMessage("user", content));
        messages.add(new GPTRequestMessage("system", OPEN_AI_SETTING));
        gptRequestAttrs.setMessages(messages);

        String requestBody = objectMapper.writeValueAsString(gptRequestAttrs);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        return requestEntity;
    }



    public class OpenAIException extends RestClientException {
        public OpenAIException(String message) {
            super(message);
        }

        public OpenAIException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
