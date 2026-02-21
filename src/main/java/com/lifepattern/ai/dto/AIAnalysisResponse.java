package com.lifepattern.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIAnalysisResponse {
    
    private String userId;
    private Integer burnoutScore;
    private String riskLevel;
    private String suggestionText;
    private LocalDateTime analyzedAt;
}
