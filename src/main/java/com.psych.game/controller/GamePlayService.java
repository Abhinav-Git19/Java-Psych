package com.psych.game.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/*
GamePlayService: to act as
 */
@Controller
@RequestMapping("/")
public class GamePlayService {

    @GetMapping("")
    public String index(){
        return "index.html"; //Return  static html page
    }
}
