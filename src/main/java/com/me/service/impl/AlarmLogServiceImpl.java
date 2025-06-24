package com.me.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.me.dao.AlarmLogDao;
import com.me.entity.AlarmLog;
import com.me.entity.User;
import com.me.service.AlarmLogService;
import com.me.utils.Message;
import com.me.utils.PageResult;
import com.me.utils.Result;
import com.me.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlarmLogServiceImpl implements AlarmLogService {
    private final AlarmLogDao alarmLogDao;

    @Override
    public Result<String> alarmStatus(Integer alarmId) {
        alarmLogDao.updateStatus(alarmId);
        return Result.success(Message.SUCCESS);
    }

    @Override
    public PageResult getAlarmLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime, Integer start, Integer size,String type) {
        User user = UserHolder.getUser();
        Integer elderId = user.getElderId();
        PageHelper.startPage(start,size);
        Page<AlarmLog> result = alarmLogDao.getAlarmLogsByTimeRange(elderId,startTime,endTime,type);
        return new PageResult(result.getTotal(),result.getResult());
    }
}
