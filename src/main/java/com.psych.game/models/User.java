package com.psych.game.models;

import com.psych.game.models.Auditable;
import com.psych.game.models.Roles;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

//User is abstract class over different kinds of User Psych going to have (Player ,ContentWriter, Admin etc.)
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) //When entity inherits from another entity, it will inherit its columns
public abstract class User extends Auditable {
    //@Email is a sort of email validator
    @Email
    @NotBlank
    @Column(unique = true) //Make column values unique
    @Getter
    @Setter
    private String email;

    @NotBlank
    @Getter
    @Setter
    private String saltedHashedPassword;

    //A User can have multiple roles
    //Note that user and role are in a many to many relationship. That needs to be declared to JPA :P
    @ManyToMany
    @Getter @Setter
    Set<Roles> roles = new HashSet<>();
}
