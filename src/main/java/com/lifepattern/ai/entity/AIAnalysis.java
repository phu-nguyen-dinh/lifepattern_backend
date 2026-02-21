package com.lifepattern.ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_analysis")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIAnalysis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "burnout_score", nullable = false)
    private Integer burnoutScore;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 20)
    private RiskLevel riskLevel;
    
    @Column(name = "suggestion_text", columnDefinition = "TEXT", nullable = false)
    private String suggestionText;
    
    @CreationTimestamp
    @Column(name = "analyzed_at", nullable = false, updatable = false)
    private LocalDateTime analyzedAt;
    
    public enum RiskLevel {
        LOW, MEDIUM, HIGH
    }
}
