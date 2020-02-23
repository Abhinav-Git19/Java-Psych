package com.psych.game.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "playeranswers")
public class PlayerAnswer extends Auditable{

    @NotNull
    @ManyToOne
    private Round round;

    @NotNull
    @ManyToOne
    private Player player;

    @NotNull
    private String answer;


}
