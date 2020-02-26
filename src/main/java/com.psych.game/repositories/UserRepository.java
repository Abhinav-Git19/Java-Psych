package com.psych.game.repositories;

import com.psych.game.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //Note that User is not a concrete class
public interface UserRepository extends JpaRepository<User,Long> {
    //Email as has to be valid field
    Optional<User> findByEmail(String email);
}
