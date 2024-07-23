package WELCOME.EMRSERVICE.Voice;

import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class TranscriptController {

    private static final Logger logger = LoggerFactory.getLogger(TranscriptController.class);

    @PostMapping("/log")
    public void logTranscript(@RequestBody TranscriptRequest transcriptRequest) {
        logger.info("Received transcript: {}", transcriptRequest.getTranscript());
    }

    public static class TranscriptRequest {
        private String transcript;

        public String getTranscript() {
            return transcript;
        }

        public void setTranscript(String transcript) {
            this.transcript = transcript;
        }
    }
}
