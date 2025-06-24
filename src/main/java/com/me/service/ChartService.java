package com.me.service;

import com.me.utils.Result;
import com.me.vo.record.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ChartService {
    Result<List<WaterUsageRecord>> findWaterUsageRange(LocalDateTime startDate, LocalDateTime endDate);

    Result<List<TemperatureRecord>> findTemperatureRange(LocalDateTime startDate, LocalDateTime endDate);

    Result<List<HomeTimesRecord>> findHomeTimesRange(LocalDateTime startDate, LocalDateTime endDate);

    Result<List<OutTimesRecord>> findOutTimesRange(LocalDateTime startDate, LocalDateTime endDate);

    Result<List<SmogLevelRecord>> findSmogLevelRange(LocalDateTime startDate, LocalDateTime endDate);

    Result<List<TurnOverRecord>> findTurnOverRange(LocalDateTime startDate, LocalDateTime endDate);
}
