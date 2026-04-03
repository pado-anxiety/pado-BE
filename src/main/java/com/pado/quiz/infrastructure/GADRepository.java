package com.pado.quiz.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GADRepository extends JpaRepository<GADEntity, Long> {

    @Query("select avg(g.score) from GADEntity g")
    Double getAverageScore();
}
