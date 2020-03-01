package com.psych.game.controller;


import com.psych.game.models.Game;
import com.psych.game.models.GameMode;
import com.psych.game.models.Player;
import com.psych.game.models.Question;
import com.psych.game.models.Round;
import com.psych.game.models.User;


import com.psych.game.repositories.GameRepository;
import com.psych.game.repositories.PlayerRepository;
import com.psych.game.repositories.QuestionRepository;
import com.psych.game.repositories.RoundRepository;
import com.psych.game.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// RestController ->Allows you to manipulate entities in JSON
@RestController
@RequestMapping("/dev-test")
public class DevTestController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoundRepository roundRepository;


    @GetMapping("/")
    public String hello(){
        return "Let's get ready to Psych!!";
    }

    @GetMapping("/populate")
    public String poupulateDB(){

        /*
        This is done to inorder to maintain the foreign key constraint
         */
        for(Player player: playerRepository.findAll()) {
            player.getGames().clear();
            playerRepository.save(player);
        }
        gameRepository.deleteAll();
        playerRepository.deleteAll();
        questionRepository.deleteAll();

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

        Game game=new Game();
        game.setGameMode(GameMode.IS_THIS_A_FACT);
        game.setLeader(luffy);
        game.getPlayers().add(luffy);

        gameRepository.save(game); //Simply adding it as such results in recursion in jackson when game endpoint is called
        //We'll be needing Json Referencing



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


    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable(name="id") Long id){
        return userRepository.findById(id).orElseThrow();
    }

    @GetMapping("/players")
    public List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }
    @GetMapping("/player/{id}")
    public Player getPlayerById(@PathVariable(name="id") Long id){
        return playerRepository.findById(id).orElseThrow();
    }

    @GetMapping("/games")
    public List<Game> getAllGames(){
        return gameRepository.findAll();
    }
    @GetMapping("/game/{id}")
    public Game getGameById(@PathVariable(name="id") Long id){
        return gameRepository.findById(id).orElseThrow();
    }

    @GetMapping("/rounds")
    public List<Round> getAllRounds(){
        return roundRepository.findAll();
    }
    @GetMapping("/round/{id}")
    public Round getRoundById(@PathVariable(name="id") Long id){
        return roundRepository.findById(id).orElseThrow();
    }



    // These two functions can be created similarly for other Entities
}
