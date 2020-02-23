package com.psych.game.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

// Idea of Serializable is convert DB data to native data types that programming language can work with
@Entity
@Table(name="Players")
public class Player extends User {
    //This kinds of enforce the not null constraints
    @NotBlank
    @Getter @Setter
    private String alias;
    @Getter @Setter
    private String psychFaceURL;
    @Getter @Setter
    private String picURL;

    /*
    This indicates that Stats is fully associated with Player, so if player gets deleted/changed/added,
    states of that player should undergo the same
     */
    @OneToOne(cascade = CascadeType.ALL)
    @Getter @Setter
    private Stat stats= new Stat();

    /*
    Because of ManyToMany with games and players, this could result in JPA duplicating the game_player relationship table
    To prevent this, we use mappedBy ="players" to indicate that "players" member in Games is already taking care of this
    relationship
     */
    @ManyToMany(mappedBy = "players") //Cascading doesn't have to happen here, if game gets deleted, player should not be deleted
    @Getter @Setter
    private Set<Game> games =new HashSet<>();
}
