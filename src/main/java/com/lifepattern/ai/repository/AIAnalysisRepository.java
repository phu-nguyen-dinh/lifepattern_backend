package com.lifepattern.ai.repository;

import com.lifepattern.ai.entity.AIAnalysis;
import com.lifepattern.ai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AIAnalysisRepository extends JpaRepository<AIAnalysis, Long> {
    
    Optional<AIAnalysis> findFirstByUserOrderByAnalyzedAtDesc(User user);
}
