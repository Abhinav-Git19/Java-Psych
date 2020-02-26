package com.psych.game.models;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "contentwriters")
public class ContentWriter extends Employee{

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JsonIdentityReference
    @Getter
    @Setter
    Set<Question> editedQuestions=new HashSet<>();
}
