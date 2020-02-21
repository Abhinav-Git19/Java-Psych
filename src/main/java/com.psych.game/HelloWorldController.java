package com.psych.game;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// RestController ->Allows you to manipulate enities in JSON
@RestController
@RequestMapping("/dev-test")
public class HelloWorldController {
    @GetMapping("/")
    public String hello(){
        return "Let's get ready to Psych!!";
    }
}
