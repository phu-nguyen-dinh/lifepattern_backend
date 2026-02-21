package com.lifepattern.ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "daily_logs", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "date"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(name = "sleep_hours", nullable = false)
    private Double sleepHours;
    
    @Column(name = "work_hours", nullable = false)
    private Double workHours;
    
    @Column(name = "study_hours", nullable = false)
    private Double studyHours;
    
    @Column(name = "entertainment_hours", nullable = false)
    private Double entertainmentHours;
    
    @Column(name = "energy_level", nullable = false)
    private Integer energyLevel;
    
    @Column(name = "stress_level", nullable = false)
    private Integer stressLevel;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
}
