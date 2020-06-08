package com.quiz.controller;

import com.quiz.model.*;
import com.quiz.repository.CompletionRepository;
import com.quiz.repository.QuestionRepository;
import com.quiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class QuizController {

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CompletionRepository completionRepo;

    //register a user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user){
        if(userRepo.findByEmail(user.getEmail()) == null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepo.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //create a quiz
    @PostMapping("/quizzes")
    public Question postQuestion(@Valid @RequestBody Question question,
                                 @AuthenticationPrincipal UserDetails userDetails){
        question.setUser(userRepo.findByEmail(userDetails.getUsername()));
        questionRepo.save(question);
        return question;
    }

    //get all quizzes
    @GetMapping("/quizzes")
    public Page<Question> getQuestions(@RequestParam(required = false) Integer page){
        if(page == null) page = 0;

        Pageable paging = PageRequest.of(page,10, Sort.by("id").ascending());
        return questionRepo.findAll(paging);
    }

    //get a quiz by id
    @GetMapping("/quizzes/{id}")
    public Question getQuestionById(@PathVariable int id){
        return questionRepo.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    //delete a quiz
    @DeleteMapping("quizzes/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable int id,
                                         @AuthenticationPrincipal UserDetails userDetails){
        Question question = questionRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(question.getUser().getEmail().equals(userDetails.getUsername())){
            questionRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    //solve a quiz by id
    @PostMapping("/quizzes/{id}/solve")
    public Feedback solveTheQuestion(@PathVariable int id, @RequestBody Answer answer,
                                     @AuthenticationPrincipal UserDetails userDetails){
        int[] correctAnswer = questionRepo.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getAnswer();
        int[] givenAnswer = answer.getAnswer();

        if(givenAnswer == null) givenAnswer = new int[0];
        if(correctAnswer == null) correctAnswer = new int[0];

        Arrays.sort(givenAnswer);
        Arrays.sort(correctAnswer);

        if(Arrays.equals(correctAnswer,givenAnswer)) {
            User user = userRepo.findByEmail(userDetails.getUsername());
            completionRepo.save(new Completion(user.getId(), id, LocalDateTime.now()));

            return new Feedback(true, "Congratulations, you're right!");
        }
        else return new Feedback(false, "Wrong answer! Please, try again.");
    }

    @GetMapping("/quizzes/completed")
    public Page<Completion> getCompletedQuizzes(@RequestParam(required = false) Integer page,
                                                @AuthenticationPrincipal UserDetails userDetails){
        User user = userRepo.findByEmail(userDetails.getUsername());

        if(page == null) page = 0;
        Pageable paging = PageRequest.of(page, 10, Sort.by("completedAt").descending());
        return completionRepo.findByUserId(user.getId(), paging);
    }
}
