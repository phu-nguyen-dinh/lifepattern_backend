package com.lifepattern.ai.controller;

import com.lifepattern.ai.dto.AIAnalysisResponse;
import com.lifepattern.ai.dto.TrendDataResponse;
import com.lifepattern.ai.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class AnalysisController {
    
    private final AnalysisService analysisService;
    
    @GetMapping("/latest")
    public ResponseEntity<AIAnalysisResponse> getLatestAnalysis(Authentication authentication) {
        String email = authentication.getName();
        AIAnalysisResponse response = analysisService.getLatestAnalysis(email);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/trends")
    public ResponseEntity<List<TrendDataResponse>> getTrends(
            Authentication authentication,
            @RequestParam(required = false) Integer days,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        String email = authentication.getName();
        List<TrendDataResponse> trends = analysisService.getTrends(email, days, start, end);
        return ResponseEntity.ok(trends);
    }
    
    @PostMapping("/regenerate")
    public ResponseEntity<AIAnalysisResponse> regenerateAnalysis(Authentication authentication) {
        String email = authentication.getName();
        AIAnalysisResponse response = analysisService.regenerateAnalysis(email);
        return ResponseEntity.ok(response);
    }
}
