package com.psych.game.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    private String saltedHashedPassword;

    /*
    You also have option to encode the password before you send to DB. This is generally considered good practice
    e.g -
    */
    public void setSaltedHashedPassword(String value){
        this.saltedHashedPassword = new BCryptPasswordEncoder().encode(value);
    }

    //A User can have multiple roles
    //Note that user and role are in a many to many relationship. That needs to be declared to JPA :P
    //Fetch will tell Spring not to load this collection in a lazy manner
    @ManyToMany(fetch = FetchType.EAGER)
    @Getter @Setter
    Set<Role> roles = new HashSet<>();

    public User(){}

    /*
    Note: Constructor for abstract class seems weird but it often used when along with the enforcement you also some
    initialization of abstract class fields
     */
    public User(User user){ //copy constructor
        email=user.getEmail();
        saltedHashedPassword=user.getSaltedHashedPassword();
        roles=user.getRoles();
    }
}
