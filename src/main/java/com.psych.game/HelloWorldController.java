package com.psych.game;

import com.psych.game.models.GameMode;
import com.psych.game.models.Player;
import com.psych.game.models.Question;
import com.psych.game.repositories.PlayerRepository;
import com.psych.game.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// RestController ->Allows you to manipulate entities in JSON
@RestController
@RequestMapping("/dev-test")
public class HelloWorldController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private QuestionRepository questionRepository;


    @GetMapping("/welcome")
    public String hello(){
        return "Let's get ready to Psych!!";
    }

    @GetMapping("/populate")
    public String poupulateDB(){

        Player luffy =new Player.Builder()
                .alias("Monkey D Luffy")
                .saltedHashedPassword("strawhat")
                .email("luffysan@onepiece.com")
                .build();

        playerRepository.save(luffy);

        Player robin =new Player.Builder()
                .alias("Nico Robin")
                .saltedHashedPassword("poneglyph")
                .email("nicopone@onepiece.com")
                .build();
        playerRepository.save(robin);

        questionRepository.save(new Question(
                "What is the most important poneglyph",
                "Rio poneglyph",
                GameMode.IS_THIS_A_FACT
        ));


        return "populated";
    }

    @GetMapping("/questions")
    public List<Question> getAllQuestions(){
        return questionRepository.findAll();
    }
    @GetMapping("/question/{id}")
    public Question getQuestionById(@PathVariable(name="id") Long id){
        return questionRepository.findById(id).orElseThrow(); //Throw exception on question with given id not being found
    }

    @GetMapping("/players")
    public List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }
    @GetMapping("/player/{id}")
    public Player getPlayerById(@PathVariable(name="id") Long id){
        return playerRepository.findById(id).orElseThrow();
    }

    // These two functions can be created similarly for other Entities
}
