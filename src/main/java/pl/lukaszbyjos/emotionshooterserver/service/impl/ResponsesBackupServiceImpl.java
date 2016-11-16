package pl.lukaszbyjos.emotionshooterserver.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pl.lukaszbyjos.emotionshooterserver.domain.VisionResponse;
import pl.lukaszbyjos.emotionshooterserver.service.ResponsesBackupService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class ResponsesBackupServiceImpl implements ResponsesBackupService {

    private static final String backupFileName = "backup.json";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void saveResponse(VisionResponse visionResponse) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(backupFileName, true))) {
            bw.write(objectMapper.writeValueAsString(visionResponse));
            bw.write(",");
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

