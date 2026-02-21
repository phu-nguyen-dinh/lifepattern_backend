package com.lifepattern.ai.controller;

import com.lifepattern.ai.dto.DailyLogRequest;
import com.lifepattern.ai.dto.DailyLogResponse;
import com.lifepattern.ai.service.DailyLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogsController {
    
    private final DailyLogService dailyLogService;
    
    @GetMapping
    public ResponseEntity<List<DailyLogResponse>> getAllLogs(Authentication authentication) {
        String email = authentication.getName();
        List<DailyLogResponse> logs = dailyLogService.getAllLogs(email);
        return ResponseEntity.ok(logs);
    }
    
    @PostMapping
    public ResponseEntity<DailyLogResponse> createLog(
            Authentication authentication,
            @Valid @RequestBody DailyLogRequest request) {
        String email = authentication.getName();
        DailyLogResponse response = dailyLogService.createLog(email, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DailyLogResponse> getLogById(
            Authentication authentication,
            @PathVariable Long id) {
        String email = authentication.getName();
        DailyLogResponse response = dailyLogService.getLogById(email, id);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteLog(
            Authentication authentication,
            @PathVariable Long id) {
        String email = authentication.getName();
        dailyLogService.deleteLog(email, id);
        return ResponseEntity.ok(Map.of("message", "Log deleted successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DailyLogResponse> updateLog(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody DailyLogRequest request) {
        String email = authentication.getName();
        DailyLogResponse response = dailyLogService.updateLog(email, id, request);
        return ResponseEntity.ok(response);
    }
}
