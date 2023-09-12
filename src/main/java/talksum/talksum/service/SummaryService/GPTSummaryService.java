package talksum.talksum.service.SummaryService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import talksum.talksum.service.SummaryService.SummaryService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GPTSummaryService implements SummaryService {

    @Value("${summary.apikey}")
    private String ApiKey;

    // 예외처리하기
    @Override
    public String executeSummary(String content) throws IOException, InterruptedException {
        String summary = "";
        HttpRequest httpRequest = makeRequest(content);
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200){
            GPTResponse gptResponse = new ObjectMapper().readValue(response.body(), GPTResponse.class);
            String tmp = gptResponse.getChoices()[gptResponse.getChoices().length - 1].getText();
            if(!tmp.isEmpty()){
                summary += tmp.replace("\n", "").trim();
            }
        }
        else{
            System.out.println(response.statusCode());
            System.out.println(response.body());
        }
        return summary;
    }

    // Request 객체 만들기
    public HttpRequest makeRequest(String content){
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(content))
                .build();
        return httpRequest;
    }

}
