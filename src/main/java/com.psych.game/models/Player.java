package com.psych.game.models;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

// Idea of Serializable is convert DB data to native data types that programming language can work with
@Entity
@Table(name= "players")
public class Player extends User {
    //This kind of enforce the not null constraints
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
    @JsonManagedReference
    @Getter @Setter
    private Stat stats= new Stat();

    /*
    Because of ManyToMany with games and players, this could result in JPA duplicating the game_player relationship table
    To prevent this, we use mappedBy ="players" to indicate that "players" member in Games is already taking care of this
    relationship
     */
    @ManyToMany(mappedBy = "players") //Cascading doesn't have to happen here, if game gets deleted, player should not be deleted
    @JsonIdentityReference //This is prevent unintended expansions
    @Getter @Setter
    private Set<Game> games =new HashSet<>();

    // This is required as Spring works with default constructors of entities even though it goes against Builder Pattern
    public Player(){}

    private Player(Builder builder) {
        setEmail(builder.email);
        setSaltedHashedPassword(builder.saltedHashedPassword);
        alias = builder.alias;
        psychFaceURL = builder.psychFaceURL;
        picURL = builder.picURL;


    }

    public Game getCurrentGame() {
        //todo
        return new Game();
    }


    public static final class Builder {
        private @Email @NotBlank String email;
        private @NotBlank String saltedHashedPassword;
        private @NotBlank String alias;
        private String psychFaceURL;
        private String picURL;

        public Builder() {
        }

        public Builder email(@Email @NotBlank String val) {
            email = val;
            return this;
        }

        public Builder saltedHashedPassword(@NotBlank String val) {
            saltedHashedPassword = val;
            return this;
        }

        public Builder alias(@NotBlank String val) {
            alias = val;
            return this;
        }

        public Builder psychFaceURL(String val) {
            psychFaceURL = val;
            return this;
        }

        public Builder picURL(String val) {
            picURL = val;
            return this;
        }

        public Player build() {
            return new Player(this);
        }
    }
}
