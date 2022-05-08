package dev.roder.intqtoolbackend.Controllers;

import dev.roder.intqtoolbackend.Entities.Notification;
import dev.roder.intqtoolbackend.Entities.QuizAnswer;
import dev.roder.intqtoolbackend.MessageWrapper.MessageContent;
import dev.roder.intqtoolbackend.Services.QuizService;
import dev.roder.intqtoolbackend.Services.UserService;
import dev.roder.intqtoolbackend.Services.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * class to serve endpoints for subscriptions for websockets, and
 * all endpoints that interact with the subscriptions.
 *
 */
@RestController
public class WebSocketController {

    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private QuizService quizService;
    @Autowired
    private UserService userService;
    @Autowired
    private SimpMessagingTemplate template;
    Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());


    /**
     * Serves as a subscription-path for sockets, and retrieves a list of the quizAnswers on subscribe.
     * Accessible for users with the role ROLE_ADMIN or ROLE_TEACHER
     *
     * @param id Id of the deployedQuiz to subscribe to its answers.
     * @return Returns a message containing a list of all answers that are kept in the deployed quiz and has the status of either submitted or graded.
     */
    @SubscribeMapping("/quizanswers/{id}")
    public MessageContent findAllQuizAnswers(@DestinationVariable Long id) {
        MessageContent message = new MessageContent();
        message.setContent(quizService.getQuestionAnswers(id.intValue()));
        return message;
    }

    /**
     * Serves as subscription-path for sockets, nd retrieves a list of a users notifications on subscribe.
     *
     * @return Returns a users list of notifications
     * @throws AccessDeniedException if the user is not verified the AccessDeniedException error is thrown
     */
    @SubscribeMapping("/notifications")
    public List<Notification> subToNotifications() throws AccessDeniedException{
        return webSocketService.getUserNotification();
    }

    /**
     * Endpoint to submit a users answers to a deployedQuiz. Also sends update to sockets subscribed to the answers of the specified deployment.
     * Accessible for users with any role.
     *
     * @param qa The answers to submit to the deployedQuiz.
     * @param deploymentId Id of the deployedQuiz to submit the answers to
     * @return Returns a response entity with StatusCode 200 or 400 based on the success of the submitting.
     */

    @RequestMapping(method = RequestMethod.POST, path = "/user/submitanswer/{deploymentId}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody ResponseEntity<String> submitUserQuiz(@RequestBody QuizAnswer qa, @PathVariable("deploymentId") Integer deploymentId) {
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try{
            QuizAnswer newlySubmittedAnswer = userService.submitUserQuizAnswer(qa, deploymentId);

            MessageContent content = new MessageContent();
            content.setContent(quizService.getQuestionAnswers(deploymentId));
            template.convertAndSend("/topic/quizanswers/"+deploymentId,content);

            response = new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            logger.warn(e.getMessage());
        }
        return response;

    }
}
