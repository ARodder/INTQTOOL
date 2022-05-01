package dev.roder.intqtoolbackend.Controllers;

import dev.roder.intqtoolbackend.Entities.DeployedQuiz;
import dev.roder.intqtoolbackend.RequestObject.GradeAnswerRequest;
import dev.roder.intqtoolbackend.Services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * This class defines endpoints relating specifically to quizzes such as creating a new quiz,
 * getting details for a specific quiz, editing or retrieving an entire quiz for a user.
 */
@Controller
@RequestMapping(path = "/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;


    /**
     * Defines endpoint to retrieve details for a specific quiz.
     * Accessible for any of the three roles.
     *
     * @param quizID id of the quiz to retrieve details from.
     * @return retrieved details.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{quizID}")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody
    String getQuiz(@PathVariable("quizID") Integer quizID) {
        return quizService.getQuiz(quizID);
    }

    /**
     * Defines endpoint for creating a new quiz and new deployment of a quiz.
     * Accessible only with roles: ROLE_TEACHER or ROLE_ADMIN
     *
     * @param quiz     deployment of the new quiz, also containing the new quiz itself.
     * @param courseId course where the quiz should be deployed.
     */

    @RequestMapping(method = RequestMethod.POST, path = "/new/{courseId}")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody
    ResponseEntity<String> createQuiz(@RequestBody DeployedQuiz quiz, @PathVariable("courseId") Integer courseId) {
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        try {
            Integer newQuizDeployId = quizService.addQuiz(quiz, courseId);
            response = new ResponseEntity<String>("" + newQuizDeployId, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }


        return response;
    }

    /**
     * Defines an endpoint for retrieving the details of a quiz deployment.
     *
     * @param deployedquizId id of the deployed quiz to retrieve details from.
     * @return returns details of the deployed quiz with the corresponding id
     */

    @RequestMapping(method = RequestMethod.GET, path = "/quizdetails/{deployedquizId}")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody
    ResponseEntity<String> getDeployedQuizDetails(@PathVariable("deployedquizId") Integer deployedquizId) {
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        try {

            response = new ResponseEntity<String>(quizService.getQuizDetails(deployedquizId), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }


        return response;
    }

    /**
     * Defines an endpoint for grading one or multiple answers to a single question.
     * Accessible only with roles: ROLE_TEACHER or ROLE_ADMIN
     *
     * @param gradeAnswerRequest Object used to get all required fields from the requestBody(List of answers to grade, feedback and grade)
     * @return Returns an updated version of the answers to the quiz.
     */

    @RequestMapping(method = RequestMethod.POST, path = "/gradeanswers")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> gradeSingleAnswer(@RequestBody GradeAnswerRequest gradeAnswerRequest) {
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            if (gradeAnswerRequest.getAnswerIds() != null && gradeAnswerRequest.getGrade() != null) {
                quizService.gradeQuizzes(gradeAnswerRequest.getAnswerIds(), gradeAnswerRequest.getGrade(), gradeAnswerRequest.getFeedback(),gradeAnswerRequest.getDeployedQuizId());
                response = new ResponseEntity<>(quizService.getQuestionAnswers(gradeAnswerRequest.getDeployedQuizId()), HttpStatus.OK);
            } else {
                throw new IllegalArgumentException("A field is missing or invalid");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return response;
        }

        return response;
    }


    @RequestMapping(method = RequestMethod.POST, path = "/save/{courseId}")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody
    ResponseEntity<String> saveUpdatedQuiz(@RequestBody DeployedQuiz updatedQuiz, @PathVariable("courseId") Integer courseId) {
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        try {
            quizService.saveQuiz(updatedQuiz, courseId);
            response = new ResponseEntity<String>("Quiz updated", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }


        return response;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/quizanswers/{deployedQuizId}")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public @ResponseBody
    ResponseEntity<String> getDeployedQuizAnswers(@PathVariable("deployedQuizId") Integer deployedQuizId) {
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        try {
            response = new ResponseEntity<String>(quizService.getQuestionAnswers(deployedQuizId), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }


        return response;
    }


}
