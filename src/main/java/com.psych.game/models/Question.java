package com.psych.game.models;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "questions")
public class Question extends  Auditable{

    @NotNull
    @Getter @Setter
    private String question;

    @NotNull
    @Getter @Setter
    private String correctAnswer;

    //This seems to be according to use case once the questions gets deleted, all associated ellenAnswers must also be
    //deleted which may or may not be the case
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "question")
    @JsonManagedReference //Tells Questions owns EllenAnswers
    @Getter @Setter
    private Set<EllenAnswer> ellenAnswers = new HashSet<>();

    //Questions would be associated with the GameMode
    // (Updated)@Enumerated(EnumType.STRING)
    @ManyToOne
    @JsonIdentityReference
    @NotNull
    @Getter @Setter
    private GameMode gameMode;

    public Question(){}


    public Question(@NotNull String question, @NotNull String correctAnswer, @NotNull GameMode gameMode) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.gameMode = gameMode;
    }
}
