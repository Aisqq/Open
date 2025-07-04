package com.me.service.analyze;

import com.me.dao.AlarmLogDao;
import com.me.dao.ElderDao;
import com.me.dao.UserDao;
import com.me.entity.AlarmLog;
import com.me.entity.Device;
import com.me.service.AnalyzeService;
import com.me.utils.Message;
import com.me.utils.ModelUtils;
import com.me.utils.SseSendUtil;
import com.me.vo.record.WaterUsageRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WaterAnalyze implements AnalyzeService {
    private final ElderDao elderDao;
    private final AlarmLogDao alarmLogDao;
    private final String waterName = Message.WATER_NAME;
    private final UserDao userDao;
    @Override
    public boolean findDeviceType(String deviceName) {
        return waterName.equals(deviceName);
    }

    @Override
    public void analyze(Device device, LocalDateTime startDate, LocalDateTime endDate, int numDays, double[] data) {
        log.info("任务用水");
        List<WaterUsageRecord> records = elderDao.findWaterUsageRange(device.getElderId(), startDate, endDate);
        for (WaterUsageRecord record : records) {
            int dayIndex = (int) ChronoUnit.DAYS.between(startDate, record.getRecordDate());
            if (dayIndex >= 0 && dayIndex < numDays) {
                data[dayIndex] = record.getWaterUsage().doubleValue();
            }
        }
        double[] scoreResult = ModelUtils.analyseFunction(data,0.3);
        if(scoreResult[0]==1){
            log.info("异常数据");
            AlarmLog alarmLog = new AlarmLog();
            alarmLog.setElderId(device.getElderId());
            alarmLog.setAlarmType(waterName);
            alarmLog.setReason(Message.WATER_REASON);
            alarmLogDao.add(alarmLog);
            Integer userId = userDao.findByElderId(device.getElderId()).getUserId();
            SseSendUtil.SseSend(userId,"老人数据异常，今天用水："+records.get(records.size()-1).getWaterUsage());
        }
    }
}
