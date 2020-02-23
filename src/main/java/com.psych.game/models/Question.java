package com.psych.game.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @Getter @Setter
    private Set<EllenAnswer> ellenAnswers;

    //Questions would be associated with the GameMode
    @Enumerated(EnumType.STRING)
    @NotNull
    @Getter @Setter
    private GameMode gameMode;

}
