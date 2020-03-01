package com.psych.game.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.psych.game.exceptions.InvalidGameActionException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "rounds")
public class Round extends Auditable{
    @ManyToOne
    @Getter @Setter
    @JsonBackReference
    @NotNull
    private Game game;

    @ManyToOne
    @Getter @Setter
    @JsonIdentityReference
    private Question question;

    @ManyToMany(cascade = CascadeType.ALL)
    @Getter @Setter
    @JsonManagedReference
    private Map<Player,PlayerAnswer> submittedAnswers =new HashMap<>();


    @ManyToMany(cascade = CascadeType.ALL) //Another way of looking at this is it indicates composition relationship
    @Getter @Setter
    @JsonManagedReference
    private Map<Player,PlayerAnswer> selectedAnswers =new HashMap<>();

    @NotNull
    @Getter @Setter
    private int roundNumber;

    // A round may or may not have ellenAnswer, but we'll worry about that later;
    @ManyToOne
    @Getter @Setter
    @JsonIdentityReference
    private EllenAnswer ellenAnswer;

    public Round() {}

    // The Ellen answer could be there or could not be there
    public Round(@NotNull Game game, Question question, @NotNull int roundNumber) {
        this.game = game;
        this.question = question;
        this.roundNumber = roundNumber;
    }


    public void submitAnswer(Player player, String answer) throws InvalidGameActionException {
        if(submittedAnswers.containsKey(player))
            throw new InvalidGameActionException("Player already submitted answer");

        //If player has already submitted answers or duplicate answers reject
        for (PlayerAnswer exisitingAnswer : submittedAnswers.values()){
            if(answer.equals(exisitingAnswer.getAnswer()))
                throw new InvalidGameActionException("Duplicate Answer");
        }
        submittedAnswers.put(player,new PlayerAnswer(this,player,answer));

    }

    public boolean allAnswersSubmitted(int numplayers) {
        return submittedAnswers.size() == numplayers;
    }


    public void selectAnswer(Player player, PlayerAnswer selectedAnswer) throws InvalidGameActionException {
        if(selectedAnswers.containsKey(player))
            throw new InvalidGameActionException("Player already selected answer");

        //User can't select your own answer
        if(selectedAnswer.getPlayer().equals(player))
            throw new InvalidGameActionException("Can't select your own answer");

        selectedAnswers.put(player,selectedAnswer);
    }

    public boolean allAnswersSelected(int numplayers) {
        return selectedAnswers.size() == numplayers;
    }
}
