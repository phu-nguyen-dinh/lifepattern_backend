package com.lifepattern.ai.repository;

import com.lifepattern.ai.entity.DailyLog;
import com.lifepattern.ai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {
    
    List<DailyLog> findByUserOrderByDateDesc(User user);
    
    Optional<DailyLog> findByIdAndUser(Long id, User user);
    
    @Query("SELECT d FROM DailyLog d WHERE d.user = :user AND d.date BETWEEN :startDate AND :endDate ORDER BY d.date ASC")
    List<DailyLog> findByUserAndDateBetween(
        @Param("user") User user,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    boolean existsByUserAndDate(User user, LocalDate date);
}
