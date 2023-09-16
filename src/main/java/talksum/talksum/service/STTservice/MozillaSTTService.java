package talksum.talksum.service.STTservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import talksum.talksum.service.ExecuteCommandService;
import talksum.talksum.service.STTservice.STTservice;

@Service
public class MozillaSTTService implements STTservice {
    @Autowired
    private ExecuteCommandService executeCommandService ;

    @Override
    public String executeSTT(String fileName) throws Exception {
        return null;
    }
}
