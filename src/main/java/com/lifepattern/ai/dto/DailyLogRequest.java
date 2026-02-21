package com.lifepattern.ai.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DailyLogRequest {
    
    @NotNull(message = "Date is required")
    private LocalDate date;
    
    @NotNull(message = "Sleep hours is required")
    @DecimalMin(value = "0.0", message = "Sleep hours must be at least 0")
    @DecimalMax(value = "24.0", message = "Sleep hours cannot exceed 24")
    private Double sleepHours;
    
    @NotNull(message = "Work hours is required")
    @DecimalMin(value = "0.0", message = "Work hours must be at least 0")
    @DecimalMax(value = "24.0", message = "Work hours cannot exceed 24")
    private Double workHours;
    
    @NotNull(message = "Study hours is required")
    @DecimalMin(value = "0.0", message = "Study hours must be at least 0")
    @DecimalMax(value = "24.0", message = "Study hours cannot exceed 24")
    private Double studyHours;
    
    @NotNull(message = "Entertainment hours is required")
    @DecimalMin(value = "0.0", message = "Entertainment hours must be at least 0")
    @DecimalMax(value = "24.0", message = "Entertainment hours cannot exceed 24")
    private Double entertainmentHours;
    
    @NotNull(message = "Energy level is required")
    @Min(value = 1, message = "Energy level must be between 1 and 10")
    @Max(value = 10, message = "Energy level must be between 1 and 10")
    private Integer energyLevel;
    
    @NotNull(message = "Stress level is required")
    @Min(value = 1, message = "Stress level must be between 1 and 10")
    @Max(value = 10, message = "Stress level must be between 1 and 10")
    private Integer stressLevel;
    
    private String notes;
}
