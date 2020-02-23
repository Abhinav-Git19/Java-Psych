package com.psych.game.repositories;

/*
Repositories allow you to provide Database related functionality inside a Java Interface (e.g select,update query with
where clause
Normally when you use Repository, you'll need to have a controller/service concrete class which will be implementing this
Repository, However @Repository annotation in Spring is going to take care of this automatically
 */

import com.psych.game.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Remember Template Syntax: Repository <Class/Entity,PrimaryKey>
@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {
}
