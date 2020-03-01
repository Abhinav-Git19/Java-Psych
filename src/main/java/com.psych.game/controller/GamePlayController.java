package com.psych.game.controller;

import com.psych.game.exceptions.InvalidGameActionException;
import com.psych.game.models.Player;
import com.psych.game.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequestMapping("/play")
public class GamePlayController {

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/")
    public String play(Authentication authentication) {
        return authentication.getName();
    }

    // GET param: /something?param-val
    // POST param: requestBody
    // URL param: /something/value/
    // abhinssi-psych.heroku.app/play/submit-answer/ asdas

    @GetMapping("/submit-answer/{answer}")
    public void submitAnswer(Authentication authentication,@PathVariable(name ="answer") String answer) throws InvalidGameActionException {
        Player player = playerRepository.findByEmail(authentication.getName()).orElseThrow();
        // Single Responsibility at play here.. One class one responsibility, anything more...delegations needs to be done
        player.getCurrentGame().submitAnswer(player,answer);
    }
}