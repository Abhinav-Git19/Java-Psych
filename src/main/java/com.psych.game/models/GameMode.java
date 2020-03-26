package com.psych.game.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

// Originally This was enum: but later changed to entity
@Entity
@Table(name = "gamemodes")
public class GameMode extends Auditable {
    @NotBlank
    @Getter
    @Setter
    @Column(unique = true)
    private String name;

    @Getter
    @Setter
    @URL
    private String picture;

    @Getter
    @Setter
    private String description;

    public GameMode() {
    }

    public GameMode(@NotBlank String name, @URL String picture, String description) {
        this.name = name;
        this.picture = picture;
        this.description = description;
    }


}

//enum is used when a code change is anticipated  when new mode is added which could to logic change
//entity -> only new row needs to be added when a new mode is added
