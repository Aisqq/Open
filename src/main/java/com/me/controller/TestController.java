package com.me.controller;


import com.me.service.AnalyzeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test")
public class TestController {
    private final AnalyzeService analyzeService;
    @GetMapping ("/t1")
    public double t1(@RequestParam String deviceId){
        return analyzeService.analyze(deviceId);
    }
}
