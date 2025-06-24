package com.me.service.impl;

import com.me.dao.ElderDao;
import com.me.entity.User;
import com.me.service.ChartService;
import com.me.utils.Message;
import com.me.utils.Result;
import com.me.utils.UserHolder;
import com.me.vo.record.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartServiceImpl implements ChartService {
    private final ElderDao elderDao;

    @Override
    public Result<List<WaterUsageRecord>> findWaterUsageRange(LocalDateTime startDate, LocalDateTime endDate) {
        User user = UserHolder.getUser();
        Integer elderId = user.getElderId();
        List<WaterUsageRecord> records = elderDao.findWaterUsageRange(elderId, startDate, endDate);
        return Result.success(Message.SUCCESS, records);
    }

    @Override
    public Result<List<TemperatureRecord>> findTemperatureRange(LocalDateTime startDate, LocalDateTime endDate) {
        User user = UserHolder.getUser();
        Integer elderId = user.getElderId();
        List<TemperatureRecord> records = elderDao.getTemperatureValueRange(elderId, startDate, endDate);
        return Result.success(Message.SUCCESS, records);
    }

    @Override
    public Result<List<HomeTimesRecord>> findHomeTimesRange(LocalDateTime startDate, LocalDateTime endDate) {
        User user = UserHolder.getUser();
        Integer elderId = user.getElderId();
        List<HomeTimesRecord> records = elderDao.countHomeTimesRange(elderId, startDate, endDate);
        return Result.success(Message.SUCCESS, records);
    }

    @Override
    public Result<List<OutTimesRecord>> findOutTimesRange(LocalDateTime startDate, LocalDateTime endDate) {
        User user = UserHolder.getUser();
        Integer elderId = user.getElderId();
        List<OutTimesRecord> records = elderDao.countOutTimesRange(elderId, startDate, endDate);
        return Result.success(Message.SUCCESS, records);
    }

    @Override
    public Result<List<SmogLevelRecord>> findSmogLevelRange(LocalDateTime startDate, LocalDateTime endDate) {
        User user = UserHolder.getUser();
        Integer elderId = user.getElderId();
        List<SmogLevelRecord> records = elderDao.getAverageSmogLevelRange(elderId, startDate, endDate);
        return Result.success(Message.SUCCESS, records);
    }

    @Override
    public Result<List<TurnOverRecord>> findTurnOverRange(LocalDateTime startDate, LocalDateTime endDate) {
        User user = UserHolder.getUser();
        Integer elderId = user.getElderId();
        List<TurnOverRecord> records = elderDao.turnOverCountRange(elderId, startDate, endDate);
        return Result.success(Message.SUCCESS, records);
    }
}
