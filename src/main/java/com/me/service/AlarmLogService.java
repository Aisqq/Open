package com.me.service;

import com.me.utils.PageResult;
import com.me.utils.Result;

import java.time.LocalDateTime;

public interface AlarmLogService {

    Result<String> alarmStatus(Integer alarmId);

    PageResult getAlarmLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime, Integer start, Integer size,String type);
}
