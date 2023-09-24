package talksum.talksum;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import talksum.talksum.service.SummaryService.GPTSummaryService;

import java.io.IOException;
import java.net.http.HttpRequest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class GPTSummaryServiceTest {



    @Autowired
    private GPTSummaryService gptSummaryService;

    @Test
    void makeRequestTest() throws JsonProcessingException {
        String content = "안녕하세요? 저는 동물매개팀의 조장 이태권입니다. 반갑습니다.\\n이번에 저희는 동물매개라는 주제를 가지고 활동을 했었는데요.\\n활동을 하면서 난항을 겪기도 했었고, 많은 어려움들이 있었습니다.\\n하지만, 우여곡절 끝에 이렇게 발표를 하게되었고, 두가지의 결과물을 가지고 왔습니다.\\n그럼, 지금부터 발표를 시작하겠습니다.\\n먼저 지도를 보시겠습니다.\\n저희는 서울시,강원도,제주도 이렇게 3개의 시,도를 다녀왔고 총 8군데를 방문하였습니다.\\n먼저 강원도부터 보시겠습니다.\\n먼저, 강원도입니다. 강원도에서는 1군데만 다녀왔는데요. 준독애견트레이닝 센터라고\\n애견트레이닝에대해 알고자 방문하였습니다. 여기 사진에보이는 분이 준독애견트레이닝센터장인\\n이준한 대표님이십니다.\\n다음으로 서울입니다.\\n";
        HttpEntity<String> res = gptSummaryService.makeRequest(content);
        System.out.println(res.getBody());
        System.out.println(res.getHeaders());
    }


    @Test
    void gptSummaryServiceTest() throws IOException, InterruptedException {
        String content = "안녕하세요? 저는 동물매개팀의 조장 이태권입니다. 반갑습니다.\\n이번에 저희는 동물매개라는 주제를 가지고 활동을 했었는데요.\\n활동을 하면서 난항을 겪기도 했었고, 많은 어려움들이 있었습니다.\\n하지만, 우여곡절 끝에 이렇게 발표를 하게되었고, 두가지의 결과물을 가지고 왔습니다.\\n그럼, 지금부터 발표를 시작하겠습니다.\\n먼저 지도를 보시겠습니다.\\n저희는 서울시,강원도,제주도 이렇게 3개의 시,도를 다녀왔고 총 8군데를 방문하였습니다.\\n먼저 강원도부터 보시겠습니다.\\n먼저, 강원도입니다. 강원도에서는 1군데만 다녀왔는데요. 준독애견트레이닝 센터라고\\n애견트레이닝에대해 알고자 방문하였습니다. 여기 사진에보이는 분이 준독애견트레이닝센터장인\\n이준한 대표님이십니다.\\n다음으로 서울입니다.\\n";
        String ret = gptSummaryService.executeSummary(content);
        System.out.println(ret);
    }

}
