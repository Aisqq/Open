package com.me.dao;

import com.github.pagehelper.Page;
import com.me.entity.AlarmLog;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

public interface AlarmLogDao {


    AlarmLog findById(Integer alarmId);

    void updateStatus(Integer alarmId);

    Page<AlarmLog> getAlarmLogsByTimeRange(@Param("elderId")Integer elderId, @Param("startTime")LocalDateTime startTime, @Param("endTime")LocalDateTime endTime,@Param("type")String type);

    void add(AlarmLog alarmLog);
}
