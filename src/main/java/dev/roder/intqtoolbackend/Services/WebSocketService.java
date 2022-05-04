package dev.roder.intqtoolbackend.Services;

import dev.roder.intqtoolbackend.Entities.Notification;
import dev.roder.intqtoolbackend.MessageWrapper.MessageContent;
import dev.roder.intqtoolbackend.Repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class WebSocketService {
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserService userservice;

    public void updateWebSocketSubscribers(String message,String socketPath){
        MessageContent content = new MessageContent();
        content.setContent(message);
        template.convertAndSend(socketPath,content);
    }

    public List<Notification> getUserNotification() throws AccessDeniedException {

        return notificationRepository.findNotificationsByUserId(userservice.getCurrentUser().getId());
    }

}
