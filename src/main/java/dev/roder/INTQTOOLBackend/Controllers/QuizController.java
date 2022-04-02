package dev.roder.INTQTOOLBackend.Controllers;

import dev.roder.INTQTOOLBackend.Entities.Quiz;
import dev.roder.INTQTOOLBackend.Services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    public Quiz createQuiz() {
        Quiz quiz = new Quiz();
        return quiz;
    }

    public void addQuestion() {

    }

    public void deleteQuestion() {

    }


}
