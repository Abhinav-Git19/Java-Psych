package com.psych.game.repositories;

import com.psych.game.models.EllenAnswer;
import com.psych.game.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EllenAnswerRepository  extends JpaRepository<EllenAnswer,Long> {

    @Query(value = "SELECT * FROM ellenanswers WHERE question = :question ORDER BY RAND() LIMIT 1",nativeQuery = true)
    EllenAnswer getRandomEllenAnswer(Question question);
}
