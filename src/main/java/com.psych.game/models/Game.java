package com.psych.game.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "games")
public class Game extends Auditable{

    @ManyToMany
    @Getter @Setter
    private Set<Player> players;

    @Getter @Setter
    @Enumerated(EnumType.STRING) //This is to tell that this ENUM in java needs to be stored as String in Database
    @NotNull
    private GameMode gameMode;

    //It matters in which order the rounds is going to come up..so it would be store in List
    // If game is deleted, rounds must be deleted
    @OneToMany(mappedBy = "game" ,cascade = CascadeType.ALL) //This mapping will automatically tell JPA to index rounds table to according to gameId
    @Getter @Setter
    private List<Round> rounds=new ArrayList<>();


    @Getter @Setter
    private int numRounds=10;
    @Getter @Setter
    private Boolean hasEllen;

    @NotNull
    @Getter @Setter
    @ManyToOne
    private Player leader;

    /*
    Why manytomany mapping? Same game can have multiple player stats and same player can have different stats relating
    to different game
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @Getter @Setter
    private Map<Player,Stat> playerStats=new HashMap<>();

    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private GameStatus gameStatus;

    @ManyToMany
    @Getter @Setter
    private Set<Player> readyPlayers=new HashSet<>();//This is to indicate that which players ready in the game for a round
}
