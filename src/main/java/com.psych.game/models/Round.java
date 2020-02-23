package com.psych.game.models;

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
    @NotNull
    private Game game;

    @ManyToOne
    @Getter @Setter
    private Question question;

    @ManyToMany
    @Getter @Setter
    private Map<Player,PlayerAnswer> playerAnswers =new HashMap<>();


    @ManyToMany(cascade = CascadeType.ALL) //Another way of looking at this is it indicates composition relationship
    @Getter @Setter
    private Map<Player,PlayerAnswer> selectedAnswers =new HashMap<>();

    @NotNull
    @Getter @Setter
    private int roundNumber;

    // A round may or may not have ellenAnswer, but we'll worry about that later;
    @ManyToOne
    @Getter @Setter
    private EllenAnswer ellenAnswer;


}
