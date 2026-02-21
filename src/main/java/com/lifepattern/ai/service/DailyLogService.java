package com.lifepattern.ai.service;

import com.lifepattern.ai.dto.DailyLogRequest;
import com.lifepattern.ai.dto.DailyLogResponse;
import com.lifepattern.ai.entity.DailyLog;
import com.lifepattern.ai.entity.User;
import com.lifepattern.ai.exception.BadRequestException;
import com.lifepattern.ai.exception.ResourceNotFoundException;
import com.lifepattern.ai.repository.DailyLogRepository;
import com.lifepattern.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyLogService {
    
    private final DailyLogRepository dailyLogRepository;
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public List<DailyLogResponse> getAllLogs(String email) {
        User user = getUserByEmail(email);
        
        return dailyLogRepository.findByUserOrderByDateDesc(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public DailyLogResponse createLog(String email, DailyLogRequest request) {
        User user = getUserByEmail(email);
        
        // Validate total hours
        double totalHours = request.getSleepHours() + request.getWorkHours() + 
                           request.getStudyHours() + request.getEntertainmentHours();
        if (totalHours > 24.0) {
            throw new BadRequestException("Total hours cannot exceed 24 hours per day");
        }
        
        // Check if log already exists for this date
        if (dailyLogRepository.existsByUserAndDate(user, request.getDate())) {
            throw new BadRequestException("A log already exists for this date");
        }
        
        var dailyLog = DailyLog.builder()
                .user(user)
                .date(request.getDate())
                .sleepHours(request.getSleepHours())
                .workHours(request.getWorkHours())
                .studyHours(request.getStudyHours())
                .entertainmentHours(request.getEntertainmentHours())
                .energyLevel(request.getEnergyLevel())
                .stressLevel(request.getStressLevel())
                .notes(request.getNotes())
                .build();
        
        dailyLogRepository.save(dailyLog);
        return mapToResponse(dailyLog);
    }
    
    @Transactional(readOnly = true)
    public DailyLogResponse getLogById(String email, Long id) {
        User user = getUserByEmail(email);
        
        DailyLog dailyLog = dailyLogRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Daily log not found with id: " + id));
        
        return mapToResponse(dailyLog);
    }
    
    @Transactional
    public void deleteLog(String email, Long id) {
        User user = getUserByEmail(email);
        
        DailyLog dailyLog = dailyLogRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Daily log not found with id: " + id));
        
        dailyLogRepository.delete(dailyLog);
    }
    
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public DailyLogResponse updateLog(String email, Long id, DailyLogRequest request) {
        User user = getUserByEmail(email);
        
        DailyLog dailyLog = dailyLogRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Daily log not found with id: " + id));
        
        // Validate total hours
        double totalHours = request.getSleepHours() + request.getWorkHours() + 
                        request.getStudyHours() + request.getEntertainmentHours();
        if (totalHours > 24.0) {
            throw new BadRequestException("Total hours cannot exceed 24 hours per day");
        }
        
        // Check if date changed and if new date conflicts with existing log
        if (!dailyLog.getDate().equals(request.getDate())) {
            if (dailyLogRepository.existsByUserAndDate(user, request.getDate())) {
                throw new BadRequestException("A log already exists for this date");
            }
        }
        
        // Update all fields
        dailyLog.setDate(request.getDate());
        dailyLog.setSleepHours(request.getSleepHours());
        dailyLog.setWorkHours(request.getWorkHours());
        dailyLog.setStudyHours(request.getStudyHours());
        dailyLog.setEntertainmentHours(request.getEntertainmentHours());
        dailyLog.setEnergyLevel(request.getEnergyLevel());
        dailyLog.setStressLevel(request.getStressLevel());
        dailyLog.setNotes(request.getNotes());
        
        dailyLogRepository.save(dailyLog);
        return mapToResponse(dailyLog);
    }
    
    private DailyLogResponse mapToResponse(DailyLog dailyLog) {
        return DailyLogResponse.builder()
                .id(dailyLog.getId().toString())
                .date(dailyLog.getDate())
                .sleepHours(dailyLog.getSleepHours())
                .workHours(dailyLog.getWorkHours())
                .studyHours(dailyLog.getStudyHours())
                .entertainmentHours(dailyLog.getEntertainmentHours())
                .energyLevel(dailyLog.getEnergyLevel())
                .stressLevel(dailyLog.getStressLevel())
                .notes(dailyLog.getNotes())
                .build();
    }

    
}
