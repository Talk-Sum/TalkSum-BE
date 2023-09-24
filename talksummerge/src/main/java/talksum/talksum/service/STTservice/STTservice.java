package talksum.talksum.service.STTservice;

public interface STTservice {

    String executeSTT(String fileName, String fileExtension, String language) throws Exception;
}
