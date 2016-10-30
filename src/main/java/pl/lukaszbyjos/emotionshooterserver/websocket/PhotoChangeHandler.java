package pl.lukaszbyjos.emotionshooterserver.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pl.lukaszbyjos.emotionshooterserver.domain.VisionResponse;

import javax.annotation.PostConstruct;

@Component
public class PhotoChangeHandler extends TextWebSocketHandler {
    private WebSocketSession session;
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    private void init() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    // This will send only to one client(most recently connected)
    public void sendProcessingInfo() {
        System.out.println("Trying to send processing info...");
        if (session != null && session.isOpen()) {
            try {
                System.out.println("Sending processing info");
                session.sendMessage(new TextMessage(
                        objectMapper.writeValueAsString(ProcessingInfo.builder().processing(true).build())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Don't have open session to send processing info");
        }
    }

    public void sendNewPhotoData(final VisionResponse visionResponse) {
        System.out.println("Trying to send vision data...");
        if (session != null && session.isOpen()) {
            try {
                System.out.println("Sending vision data");
                session.sendMessage(new TextMessage(
                        objectMapper.writeValueAsString(visionResponse)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Don't have open session to send vision data");
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("Connection established");
        this.session = session;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        if ("CLOSE".equalsIgnoreCase(message.getPayload())) {
            session.close();
        } else {
            System.out.println("Received:" + message.getPayload());
        }
    }

}

