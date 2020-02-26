package com.psych.game.controller;

import com.psych.game.models.Player;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequestMapping("/play")
public class GamePlayController {

    @GetMapping("/")
    public String play(Authentication authentication) {
        return authentication.getName();
    }

    public void submitAnswer(Player player, String answer){

    }
}