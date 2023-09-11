package talksum.talksum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MozillaSTTService implements STTservice{
    @Autowired
    private ExecuteCommandService executeCommandService ;

    @Override
    public String executeSTT() {
        return null;
    }
}
