package com.frogcrew.frogcrew.repository;

import com.frogcrew.frogcrew.domain.model.FinancialTransaction;
import com.frogcrew.frogcrew.domain.model.Game;
import com.frogcrew.frogcrew.domain.model.FrogCrewUser;
import com.frogcrew.frogcrew.domain.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, UUID> {

    @Query("SELECT SUM(ft.total) FROM FinancialTransaction ft WHERE ft.game.season = :season")
    BigDecimal getTotalForSeason(@Param("season") String season);

    @Query("SELECT ft.game, SUM(ft.total) FROM FinancialTransaction ft WHERE ft.game.season = :season GROUP BY ft.game")
    List<Object[]> getTotalsByGameForSeason(@Param("season") String season);

    @Query("SELECT ft.user, COUNT(ft.game), SUM(ft.hours) FROM FinancialTransaction ft " +
            "WHERE ft.position.code = :positionCode AND ft.game.season = :season " +
            "GROUP BY ft.user")
    List<Object[]> getPositionStats(@Param("positionCode") String positionCode, @Param("season") String season);

    @Query("SELECT ft.game, ft.position, ft.hours, ft.rate, ft.total FROM FinancialTransaction ft " +
            "WHERE ft.user.id = :userId AND ft.game.season = :season")
    List<Object[]> getUserFinancialDetails(@Param("userId") UUID userId, @Param("season") String season);
}