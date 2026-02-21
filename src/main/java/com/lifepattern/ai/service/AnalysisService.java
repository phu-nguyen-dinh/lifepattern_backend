package com.lifepattern.ai.service;

import com.lifepattern.ai.dto.AIAnalysisResponse;
import com.lifepattern.ai.dto.TrendDataResponse;
import com.lifepattern.ai.entity.AIAnalysis;
import com.lifepattern.ai.entity.DailyLog;
import com.lifepattern.ai.entity.User;
import com.lifepattern.ai.exception.BadRequestException;
import com.lifepattern.ai.exception.ResourceNotFoundException;
import com.lifepattern.ai.repository.AIAnalysisRepository;
import com.lifepattern.ai.repository.DailyLogRepository;
import com.lifepattern.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    
    private final AIAnalysisRepository aiAnalysisRepository;
    private final DailyLogRepository dailyLogRepository;
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public AIAnalysisResponse getLatestAnalysis(String email) {
        User user = getUserByEmail(email);
        
        AIAnalysis analysis = aiAnalysisRepository.findFirstByUserOrderByAnalyzedAtDesc(user)
                .orElseThrow(() -> new ResourceNotFoundException("No analysis found. Please create a daily log first."));
        
        return mapToResponse(analysis);
    }
    
    @Transactional(readOnly = true)
    public List<TrendDataResponse> getTrends(String email, Integer days, LocalDate start, LocalDate end) {
        User user = getUserByEmail(email);
        
        LocalDate startDate;
        LocalDate endDate;
        
        if (start != null && end != null) {
            startDate = start;
            endDate = end;
        } else if (days != null && days > 0) {
            endDate = LocalDate.now();
            startDate = endDate.minusDays(days - 1);
        } else {
            throw new BadRequestException("Please provide either 'days' or both 'start' and 'end' dates");
        }
        
        List<DailyLog> logs = dailyLogRepository.findByUserAndDateBetween(user, startDate, endDate);
        
        return logs.stream()
                .map(log -> TrendDataResponse.builder()
                        .date(log.getDate().toString())
                        .sleep(log.getSleepHours())
                        .stress(log.getStressLevel())
                        .build())
                .collect(Collectors.toList());
    }
    
    @Transactional
    public AIAnalysisResponse regenerateAnalysis(String email) {
        User user = getUserByEmail(email);
        
        // Get the most recent daily log
        List<DailyLog> recentLogs = dailyLogRepository.findByUserOrderByDateDesc(user);
        
        if (recentLogs.isEmpty()) {
            throw new BadRequestException("No daily logs found. Please create a log first.");
        }
        
        DailyLog latestLog = recentLogs.get(0);
        
        // Calculate burnout score using the specified formula
        // score = (workHours * 5) + (stressLevel * 5) - (sleepHours * 3)
        double rawScore = (latestLog.getWorkHours() * 5) + 
                         (latestLog.getStressLevel() * 5) - 
                         (latestLog.getSleepHours() * 3);
        
        // Clamp between 0-100
        int burnoutScore = (int) Math.max(0, Math.min(100, rawScore));
        
        // Determine risk level
        AIAnalysis.RiskLevel riskLevel;
        if (burnoutScore < 40) {
            riskLevel = AIAnalysis.RiskLevel.LOW;
        } else if (burnoutScore < 70) {
            riskLevel = AIAnalysis.RiskLevel.MEDIUM;
        } else {
            riskLevel = AIAnalysis.RiskLevel.HIGH;
        }
        
        // Generate suggestion text
        String suggestionText = generateSuggestion(riskLevel, latestLog);
        
        // Create and save the analysis
        var analysis = AIAnalysis.builder()
                .user(user)
                .burnoutScore(burnoutScore)
                .riskLevel(riskLevel)
                .suggestionText(suggestionText)
                .build();
        
        aiAnalysisRepository.save(analysis);
        
        return mapToResponse(analysis);
    }
    
    private String generateSuggestion(AIAnalysis.RiskLevel riskLevel, DailyLog log) {
        switch (riskLevel) {
            case LOW:
                return "Great job maintaining balance! Your current routine shows healthy work-life balance. " +
                       "Keep prioritizing " + log.getSleepHours() + " hours of sleep and managing stress effectively.";
            
            case MEDIUM:
                return "You're showing moderate signs of stress. Consider reducing work hours (" + 
                       log.getWorkHours() + "h currently) and increasing sleep time. " +
                       "Try relaxation techniques and ensure you're taking regular breaks.";
            
            case HIGH:
                return "Warning: High burnout risk detected! Your work hours (" + log.getWorkHours() + 
                       "h) and stress level (" + log.getStressLevel() + "/10) are concerning. " +
                       "Prioritize rest (current: " + log.getSleepHours() + "h sleep). " +
                       "Consider speaking with a healthcare professional and adjusting your schedule.";
            
            default:
                return "Unable to generate suggestion.";
        }
    }
    
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    private AIAnalysisResponse mapToResponse(AIAnalysis analysis) {
        return AIAnalysisResponse.builder()
                .userId(analysis.getUser().getId().toString())
                .burnoutScore(analysis.getBurnoutScore())
                .riskLevel(analysis.getRiskLevel().name())
                .suggestionText(analysis.getSuggestionText())
                .analyzedAt(analysis.getAnalyzedAt())
                .build();
    }
}
