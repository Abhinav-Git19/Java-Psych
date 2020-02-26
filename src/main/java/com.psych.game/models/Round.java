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
    private Map<Player,PlayerAnswer> playerAnswers =new HashMap<>();


    @ManyToMany(cascade = CascadeType.ALL) //Another way of looking at this is it indicates composition relationship
    @Getter @Setter
    @JsonManagedReference
    private Map<Player,PlayerAnswer> submittedAnswers =new HashMap<>();

    @NotNull
    @Getter @Setter
    private int roundNumber;

    // A round may or may not have ellenAnswer, but we'll worry about that later;
    @ManyToOne
    @Getter @Setter
    @JsonIdentityReference
    private EllenAnswer ellenAnswer;


    public void submitAnswer(Player player, String answer) throws InvalidGameActionException {
        if(submittedAnswers.containsKey(player))
            throw new InvalidGameActionException("Playere already submitted answere");

        for (PlayerAnswer exisitingAnswer : submittedAnswers.values()){
            throw new InvalidGameActionException("Duplicate Answer");
        }
        //If player has already submitted answers or duplicate answers reject
    }

    public boolean allAnswersSubmitted(int numplayers) {
        return submittedAnswers.size() == numplayers;
    }
}
