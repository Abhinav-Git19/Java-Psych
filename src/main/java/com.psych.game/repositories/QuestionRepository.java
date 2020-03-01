package com.psych.game.repositories;

import com.psych.game.models.GameMode;
import com.psych.game.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {

    //@Query maps your actual query to jpa query here
    //nativeQuery options tells that this is to be your actual query to be run on your database
    @Query(value = "SELECT * FROM questions WHERE gameMode = :gameMode ORDER BY RAND() LIMIT 1",nativeQuery = true)
    Question getRandomQuestion(GameMode gameMode);
}
