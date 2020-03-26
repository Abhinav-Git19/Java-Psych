package com.psych.game.controller;

import com.psych.game.models.Game;
import com.psych.game.models.GameMode;
import com.psych.game.models.Player;
import com.psych.game.repositories.GameModeRepository;
import com.psych.game.repositories.GameRepository;
import com.psych.game.repositories.PlayerRepository;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


// NOTE to myself: here are two broad ways of designing fronted
/*
Static Content: Use template engines
//syntax for place holders
<html>
<head>
<title> Title </title>
</head>
</html>
 */

/* Have a Reactive frontend
view files -->  html using some framework which is reactive
send AJAX calls to some endpoints --> to which backend will responde to data
the view reacts to change in data
 */




//Declaring it as a service prevents us to return in form of json
@RestController
@RequestMapping("/play")
public class GamePlayAPI {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameModeRepository gameModeRepository;
    @Autowired
    private GameRepository gameRepository;

    //Utility function to wrap player data in form of JSON
    private JSONObject getData(Player player){
        Game currentGame = player.getCurrentGame();
        JSONObject response = new JSONObject();
        response.put("playerAlias",player.getAlias());
        response.put("currentGame",currentGame==null?null:currentGame.getId());

        if(currentGame==null) {
            JSONArray gameModes = new JSONArray();
            for (GameMode mode : gameModeRepository.findAll()) {
                JSONObject gameMode = new JSONObject();
                gameMode.put("title", mode.getName());
                gameMode.put("image", mode.getPicture());
                gameMode.put("description", mode.getDescription());

                gameModes.add(gameMode);
            }
            response.put("gameModes", gameModes);
        }else{
            response.put("gameState",currentGame.getGameState());
        }
        return response;
    }
    private Player getCurrentPlayer(Authentication authentication){
        return playerRepository.findByEmail(authentication.getName()).orElseThrow();
    }
    // JSONObject used to return generic JSON object
    @GetMapping("/")
    public JSONObject play(Authentication authentication) {
        Player player =getCurrentPlayer(authentication);
        return getData(player);
    }
    @GetMapping("create-game")
    public JSONObject createGame(Authentication authentication,
                           @RequestParam(name = "mode") String gameMode,
                           @RequestParam(name = "rounds") Integer numRounds,
                           @RequestParam(name = "ellen") Boolean hasEllen){
        //A game requires its leader
        Player leader = getCurrentPlayer(authentication);
        GameMode mode=gameModeRepository.findByName(gameMode).orElseThrow(); //List of GameModes will be fecthed from repository
        gameRepository.save(new Game(mode, numRounds, hasEllen, leader));
        return getData(leader);
    }

    /* OLD Code for authentication
    public String play(Authentication authentication) {
        return authentication.getName();
    }

    // GET param: /something?param-val
    // POST param: requestBody
    // URL param: /something/value/
    // abhinssi-psych.heroku.app/play/submit-answer/ asdas

    @GetMapping("/submit-answer/{answer}")
    public void submitAnswer(Authentication authentication,@PathVariable(name ="answer") String answer) throws Exception, InvalidGameActionException {
        Player player = playerRepository.findByEmail(authentication.getName()).orElseThrow();
        //Single Responsibility at play here.. One class one responsibility, anything more...delegations needs to be done
        player.getCurrentGame().submitAnswer(player,answer);
    }

     */
}

