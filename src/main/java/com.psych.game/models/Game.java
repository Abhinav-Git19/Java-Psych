package com.psych.game.models;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.psych.game.Utils;
import com.psych.game.exceptions.InvalidGameActionException;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

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

    //(Updated) "Earlier this was Enum"  @Enumerated(EnumType.STRING)
    //This is to tell that this ENUM in java needs to be stored as String in Database
    @ManyToOne
    @JsonIdentityReference
    @Getter
    @Setter
    @NotNull
    private GameMode gameMode;

    //It matters in which order the rounds is going to come up..so it would be store in List
    // If game is deleted, rounds must be deleted
    @OneToMany(mappedBy = "game" ,cascade = CascadeType.ALL) //This mapping will automatically tell JPA to index rounds table to according to gameId
    @OrderBy(value = "round_number asc") //JPA converts roundNumber to round_number
    @Getter
    @Setter
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
    private GameStatus gameStatus = GameStatus.PLAYERS_JOINING;

    @ManyToMany
    @JsonIdentityReference
    @Getter @Setter
    private Set<Player> readyPlayers=new HashSet<>();//This is to indicate that which players ready in the game for a round

    //For spring
    public Game(){}

    public Game(@NotNull GameMode gameMode, int numRounds, Boolean hasEllen, @NotNull Player leader) {
        this.gameMode = gameMode;
        this.numRounds = numRounds;
        this.hasEllen = hasEllen;
        this.leader = leader;
        try {
            this.addPlayer(leader);
        } catch (InvalidGameActionException ignored) {
        }
    }


    public void addPlayer(Player player) throws InvalidGameActionException {
        /*
        Regarding Exceptions: Your exception should follow the middle ground of relating to one class only.. Or else
        your exceptions would either be too specific or too generic
         */
        if(!gameStatus.equals(GameStatus.PLAYERS_JOINING))
            throw new InvalidGameActionException("Can't join after game is started");
        this.players.add(player);
        player.setCurrentGame(this);

    }

    public void removePlayer(Player player) throws InvalidGameActionException {

        if(!players.contains(player))
            throw new InvalidGameActionException("No such player");

        if(!gameStatus.equals(GameStatus.PLAYERS_JOINING))
            throw new InvalidGameActionException("Can't leave after game is started");
        this.players.remove(player);

        if(player.getCurrentGame().equals(this))
            player.setCurrentGame(null);

        if(players.size()==0 || (players.size()==1 && !gameStatus.equals(GameStatus.PLAYERS_JOINING))){
            endGame();
        }

    }

    public void startGame(Player player) throws InvalidGameActionException {

        if(!player.equals(leader))
            throw new InvalidGameActionException("Only leader can start the game");

        startNewRound();


    }

    private void startNewRound() {
        gameStatus = GameStatus.SUBMITTING_ANSWERS;
        /*
        For Round: Question specification depends on the gameMode, then Question (and EllenAnswer if needed)
        would be fetched from DB
        */
        Question question = Utils.getRandomQuestion(gameMode);
        Round round =new Round(this,question,rounds.size());
        if(hasEllen)
            round.setEllenAnswer(Utils.getRandomEllenAnswer(question));
        rounds.add(new Round());
    }

    /*
    General flow of logic while writing the code:
    1) Validate
    2) Core of what needs to be done, generally changing one state
    Each method is ascribed to a state of Game
     */
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

    public void selectAnswer(Player player,PlayerAnswer selectedAnswer) throws InvalidGameActionException {
        if(!players.contains(player))
            throw new InvalidGameActionException("No such player");
        if(!gameStatus.equals(GameStatus.SELECTING_ANSWERS))
            throw new InvalidGameActionException("Game is not selecting answers at present");

        Round currentRound=getCurrentRound();
        currentRound.selectAnswer(player ,selectedAnswer);

        // At endRound Player need not be readu anymore
        if(currentRound.allAnswersSubmitted(players.size())){
            if(rounds.size()<numRounds)
                gameStatus=GameStatus.WAITING_FOR_READY;
            else
                endGame();
        }

    }


    public void playerIsReady(Player player) throws InvalidGameActionException {
        if(!players.contains(player))
            throw new InvalidGameActionException("No such player");
        if(!gameStatus.equals(GameStatus.WAITING_FOR_READY))
            throw new InvalidGameActionException("Game is not waiting for players to be ready");

        readyPlayers.add(player);
        if(readyPlayers.size()==players.size())
            startNewRound();
    }

    public void playerIsNotReady(Player player) throws InvalidGameActionException {
        if(!players.contains(player))
            throw new InvalidGameActionException("No such player");
        if(!gameStatus.equals(GameStatus.WAITING_FOR_READY))
            throw new InvalidGameActionException("Game is not waiting for players to be ready");

        readyPlayers.remove(player);
    }

    private Round getCurrentRound() throws InvalidGameActionException {
        if(rounds.size()==0)
            throw new InvalidGameActionException("Game has not started");
        // TODO: 22/03/20   This itself doesn't represent the correct order...Logical issue
        return rounds.get(rounds.size()-1);
    }

    private void endGame() {
        gameStatus=GameStatus.ENDED;

        //Once the game is over all players current Game session needs to be ended
        for(Player player: players){

            //This if block is to ensure that we are terminating only the current game
            //for the players whose status is reached the end game. There might be the
            // for all list of players, some of the player currentGame may not be the
            // same *current game* :p
            if(player.getCurrentGame().equals(this))
                player.setCurrentGame(null);
        }
    }

    public JSONObject getGameState(){
        JSONObject state = new JSONObject();
        state.put("id",getId());
        state.put("numRounds",numRounds);
        state.put("mode",gameMode.getName());
        JSONArray playerData = new JSONArray();

        for (Player player:players){
            JSONObject data = new JSONObject();
            data.put("alias",player.getAlias());
            playerData.add(data);
        }
        state.put("players",playerData);
        return state;

    }


}
