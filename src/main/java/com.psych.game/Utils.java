package com.psych.game;

import com.psych.game.config.ApplicationContextProvider;
import com.psych.game.models.EllenAnswer;
import com.psych.game.models.GameMode;
import com.psych.game.models.Question;
import com.psych.game.repositories.EllenAnswerRepository;
import com.psych.game.repositories.QuestionRepository;

public class Utils {

    // Note that Autowire only works when the class is service,controller or RESTController otherwise we need to have a context
    private static QuestionRepository questionRepository;

    private static EllenAnswerRepository ellenAnswerRepository;

    /*
    Here the repositories is getting the required context for the interfaces we implemented
    Interfaces require concrete class to be implemented which was taken care by SpringBoot via @Autowire
    Since autowire cannot be used here..we have to explicitly instruct Spring to get the required Bean by
    providing the required ApplicationContext specified inside our configuration package
     */
    static {
        questionRepository = (QuestionRepository) ApplicationContextProvider
                .getApplicationContext()
                .getBean("questionRepository");
        ellenAnswerRepository = (EllenAnswerRepository) ApplicationContextProvider
                .getApplicationContext()
                .getBean("ellenAnswerRepository");
    }

    public static Question getRandomQuestion(GameMode gameMode) {
        return questionRepository.getRandomQuestion(gameMode);
    }

    public static EllenAnswer getRandomEllenAnswer(Question question) {
        return ellenAnswerRepository.getRandomEllenAnswer(question);
    }
}
