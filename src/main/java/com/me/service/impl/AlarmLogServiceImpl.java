package com.me.service.impl;

import com.me.dao.AlarmLogDao;
import com.me.entity.AlarmLog;
import com.me.entity.User;
import com.me.service.AlarmLogService;
import com.me.utils.Message;
import com.me.utils.Result;
import com.me.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlarmLogServiceImpl implements AlarmLogService {
    private final AlarmLogDao alarmLogDao;
    @Override
    public Result<List<AlarmLog>> getAlarmLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        User user = UserHolder.getUser();
        Integer elderId = user.getElderId();
        return Result.success(Message.SUCCESS,alarmLogDao.getAlarmLogsByTimeRange(elderId,startTime,endTime));
    }

    @Override
    public Result<String> alarmStatus(Integer alarmId) {
        alarmLogDao.updateStatus(alarmId);
        return Result.success(Message.SUCCESS);
    }
}
