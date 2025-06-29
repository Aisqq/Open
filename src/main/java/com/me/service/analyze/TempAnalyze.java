package com.me.service.analyze;

import com.me.dao.AlarmLogDao;
import com.me.dao.ElderDao;
import com.me.entity.AlarmLog;
import com.me.entity.Device;
import com.me.service.AnalyzeService;
import com.me.utils.Message;
import com.me.utils.ModelUtils;
import com.me.vo.record.TemperatureRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TempAnalyze implements AnalyzeService {
    private final ElderDao elderDao;
    private final AlarmLogDao alarmLogDao;
    private final String tempName = Message.TEMP_NAME;
    @Override
    public boolean findDeviceType(String deviceName) {
        return tempName.equals(deviceName);
    }
    @Override
    public void analyze(Device device, LocalDateTime startDate, LocalDateTime endDate, int numDays, double[] data) {
        log.info("任务温度");
        List<TemperatureRecord> records = elderDao.getTemperatureValueRange(device.getElderId(), startDate, endDate);
        for (TemperatureRecord record : records) {
            int dayIndex = (int) ChronoUnit.DAYS.between(startDate, record.getRecordTime());
            if (dayIndex >= 0 && dayIndex < numDays) {
                data[dayIndex] = record.getTemperature().doubleValue();
            }
        }
        double[] scoreResult = ModelUtils.analyseFunction(data,0.3);
        if(scoreResult[0]==1){
            log.info("异常数据");
            AlarmLog alarmLog = new AlarmLog();
            alarmLog.setElderId(device.getElderId());
            alarmLog.setAlarmType(tempName);
            alarmLog.setReason(Message.TEMP_REASON);
            alarmLogDao.add(alarmLog);
        }
    }
}
