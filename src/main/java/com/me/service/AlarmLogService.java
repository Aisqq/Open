package com.me.service;

import com.me.entity.AlarmLog;
import com.me.utils.Result;

import java.time.LocalDateTime;
import java.util.List;

public interface AlarmLogService {
    Result<List<AlarmLog>> getAlarmLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    Result<String> alarmStatus(Integer alarmId);
}
