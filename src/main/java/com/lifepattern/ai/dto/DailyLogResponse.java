package com.lifepattern.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyLogResponse {
    
    private String id;
    private LocalDate date;
    private Double sleepHours;
    private Double workHours;
    private Double studyHours;
    private Double entertainmentHours;
    private Integer energyLevel;
    private Integer stressLevel;
    private String notes;
}
