package com.me.controller;

import com.me.service.ChartService;
import com.me.utils.Result;
import com.me.vo.record.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chart/")
public class ChartController{
    private final ChartService chartService;
    /**
     * 用水量记录接口
     */
    @GetMapping("/water-usage")
    public Result<List<WaterUsageRecord>> findWaterUsageRange(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {
        startDate = startDate.toLocalDate().atStartOfDay();
        endDate = endDate.toLocalDate().atStartOfDay().plusDays(1);
        return chartService.findWaterUsageRange(startDate, endDate);
    }

    /**
     * 体温记录接口
     */
    @GetMapping("/temperature")
    public Result<List<TemperatureRecord>> findTemperatureRange(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {
        startDate = startDate.toLocalDate().atStartOfDay();
        endDate = endDate.toLocalDate().atStartOfDay().plusDays(1);
        return chartService.findTemperatureRange(startDate, endDate);
    }

    /**
     * 回家次数统计接口
     */
    @GetMapping("/home-times")
    public Result<List<HomeTimesRecord>> findHomeTimesRange(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {
        startDate = startDate.toLocalDate().atStartOfDay();
        endDate = endDate.toLocalDate().atStartOfDay().plusDays(1);
        return chartService.findHomeTimesRange(startDate, endDate);
    }

    /**
     * 外出次数统计接口
     */
    @GetMapping("/out-times")
    public Result<List<OutTimesRecord>> findOutTimesRange(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {
        startDate = startDate.toLocalDate().atStartOfDay();
        endDate = endDate.toLocalDate().atStartOfDay().plusDays(1);
        return chartService.findOutTimesRange(startDate, endDate);
    }

    /**
     * 平均雾霾水平接口
     */
    @GetMapping("/smog-level")
    public Result<List<SmogLevelRecord>> findSmogLevelRange(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {
        startDate = startDate.toLocalDate().atStartOfDay();
        endDate = endDate.toLocalDate().atStartOfDay().plusDays(1);
        return chartService.findSmogLevelRange(startDate, endDate);
    }

    /**
     * 翻身次数统计接口
     */
    @GetMapping("/turn-over")
    public Result<List<TurnOverRecord>> findTurnOverRange(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {
        startDate = startDate.toLocalDate().atStartOfDay();
        endDate = endDate.toLocalDate().atStartOfDay().plusDays(1);
        return chartService.findTurnOverRange(startDate, endDate);
    }

    @GetMapping("/alarm-logs")
    public Result<List<Map<String, Integer>>> findAlarmLogsRange(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {
        startDate = startDate.toLocalDate().atStartOfDay();
        endDate = endDate.toLocalDate().atStartOfDay().plusDays(1);
        return chartService.findAlarmLogsRange(startDate, endDate);
    }
}
