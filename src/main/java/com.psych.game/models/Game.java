package com.psych.game.models;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.psych.game.exceptions.InvalidGameActionException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "games")
public class Game extends Auditable{

    @ManyToMany
    @JsonIdentityReference
    @Getter @Setter
    private Set<Player> players=new HashSet<>();

    @Getter @Setter
    @Enumerated(EnumType.STRING) //This is to tell that this ENUM in java needs to be stored as String in Database
    @NotNull
    private GameMode gameMode;

    //It matters in which order the rounds is going to come up..so it would be store in List
    // If game is deleted, rounds must be deleted
    @OneToMany(mappedBy = "game" ,cascade = CascadeType.ALL) //This mapping will automatically tell JPA to index rounds table to according to gameId
    @Getter @Setter
    @JsonManagedReference
    private List<Round> rounds=new ArrayList<>();


    @Getter @Setter
    private int numRounds=10;
    @Getter @Setter
    private Boolean hasEllen;

    @NotNull
    @Getter @Setter
    @JsonIdentityReference //Game Doesn't own the reference
    @ManyToOne
    private Player leader;

    /*
    Why manytomany mapping? Same game can have multiple player stats and same player can have different stats relating
    to different game
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @Getter @Setter
    private Map<Player,Stat> playerStats=new HashMap<>();

    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private GameStatus gameStatus;

    @ManyToMany
    @JsonIdentityReference
    @Getter @Setter
    private Set<Player> readyPlayers=new HashSet<>();//This is to indicate that which players ready in the game for a round

    public Game(@NotNull GameMode gameMode, int numRounds, Boolean hasEllen, @NotNull Player leader) {
        this.gameMode = gameMode;
        this.numRounds = numRounds;
        this.hasEllen = hasEllen;
        this.leader = leader;
        this.players.add(leader);
    }


    public void addPlayer(Player player) throws InvalidGameActionException {

        if(!gameStatus.equals(GameStatus.JOINING))
            throw new InvalidGameActionException("Can't join after game is started");
        this.players.add(player);

    }

    public void removePlayer(Player player) throws InvalidGameActionException {

        if(!players.contains(player))
            throw new InvalidGameActionException("No such player");

        if(!gameStatus.equals(GameStatus.JOINING))
            throw new InvalidGameActionException("Can't leave after game is started");
        this.players.remove(player);

        if(players.size()==0 || (players.size()==1 && !gameStatus.equals(GameStatus.JOINING))){
            endGame();
        }

    }

    public void startGame(Player player) throws InvalidGameActionException {

        if(!player.equals(leader))
            throw new InvalidGameActionException("Only leader can start the game");

        createNewRound();


    }

    private void createNewRound() {
        gameStatus = GameStatus.SUBMITTING_ANSWERS;
        //todo
    }

    //Here the code is local
    public void submitAnswer(Player player,String answer) throws InvalidGameActionException {
        //Validations
        if(answer.length()==0)
            throw new InvalidGameActionException("Answer Cannot be Empty");
        if(!players.contains(player))
            throw new InvalidGameActionException("No such exception");

        if(!gameStatus.equals(GameStatus.SUBMITTING_ANSWERS))
            throw new InvalidGameActionException("Game is not accepting answers");

        Round currentRound = getCurrentRound();
        currentRound.submitAnswer(player,answer);

        if(currentRound.allAnswersSubmitted(players.size())){
            gameStatus=GameStatus.SELECTING_ANSWERS;
        }
    }

    public void selectAnswer(Player player,PlayerAnswer selectedAnswer){

    }

    private Round getCurrentRound() {
        //todo
    }

    private void endGame() {
        //todo
    }


}
