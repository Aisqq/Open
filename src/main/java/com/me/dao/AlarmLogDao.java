package com.me.dao;

import com.me.entity.AlarmLog;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AlarmLogDao {
    List<AlarmLog> getAlarmLogsByTimeRange(@Param("elderId")Integer elderId, @Param("startTime")LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);

    AlarmLog findById(Integer alarmId);

    void updateStatus(Integer alarmId);
}
