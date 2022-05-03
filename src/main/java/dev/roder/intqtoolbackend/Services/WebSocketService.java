package dev.roder.intqtoolbackend.Services;

import dev.roder.intqtoolbackend.MessageWrapper.MessageContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private QuizService quizService;

    public void updateWebSocketSubscribers(String message,String socketPath){
        MessageContent content = new MessageContent();
        content.setContent(message);
        template.convertAndSend(socketPath,content);
    }

}
