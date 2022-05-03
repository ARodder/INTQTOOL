package dev.roder.intqtoolbackend.Controllers;

import dev.roder.intqtoolbackend.Entities.QuestionAnswer;
import dev.roder.intqtoolbackend.Entities.QuizAnswer;
import dev.roder.intqtoolbackend.MessageWrapper.MessageContent;
import dev.roder.intqtoolbackend.Services.QuizService;
import dev.roder.intqtoolbackend.Services.UserService;
import dev.roder.intqtoolbackend.Services.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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


    @SubscribeMapping("/quizanswers/{id}")
    public MessageContent findAllQuizAnswers(@DestinationVariable Long id) {
        MessageContent message = new MessageContent();
        message.setContent(quizService.getQuestionAnswers(id.intValue()));
        return message;
    }

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
            System.out.println(e.getMessage());
        }





        return response;

    }
}
