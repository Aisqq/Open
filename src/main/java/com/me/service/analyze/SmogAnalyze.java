package com.me.service.analyze;

import com.me.dao.AlarmLogDao;
import com.me.dao.ElderDao;
import com.me.dao.UserDao;
import com.me.entity.AlarmLog;
import com.me.entity.Device;
import com.me.service.AnalyzeService;
import com.me.utils.BigDecimalUtils;
import com.me.utils.Message;
import com.me.utils.ModelUtils;
import com.me.utils.SseSendUtil;
import com.me.vo.record.SmogLevelRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class SmogAnalyze implements AnalyzeService {
    private final ElderDao elderDao;
    private final AlarmLogDao alarmLogDao;
    private final String smogName = Message.SMOG_NAME;
    private final UserDao userDao;
    @Override
    public boolean findDeviceType(String deviceName) {
        return smogName.equals(deviceName);
    }

    @Override
    public void analyze(Device device, LocalDateTime startDate, LocalDateTime endDate, int numDays, double[] data) {
        log.info("任务烟雾");
        List<SmogLevelRecord> records = elderDao.getAverageSmogLevelRange(device.getElderId(), startDate, endDate);
        for (SmogLevelRecord record : records) {
            int dayIndex = (int) ChronoUnit.DAYS.between(startDate, record.getDate());
            if (dayIndex >= 0 && dayIndex < numDays) {
                data[dayIndex] = record.getAverageSmogLevel().doubleValue();
            }
        }
        double[] scoreResult = ModelUtils.analyseFunction(data,0.3);
        if(scoreResult[0]==1){
            log.info("异常数据");
            AlarmLog alarmLog = new AlarmLog();
            alarmLog.setElderId(device.getElderId());
            alarmLog.setAlarmType(smogName);
            alarmLog.setReason("烟雾浓度数据异常，烟雾浓度："+ BigDecimalUtils.roundToOneDecimal(records.get(records.size()-1).getAverageSmogLevel()));
            alarmLogDao.add(alarmLog);
            List<Integer> userIdList = userDao.findUserIdByElderId(device.getElderId());
            for(Integer userId:userIdList){
                try {
                    SseSendUtil.SseSend(userId,"烟雾浓度数据异常，烟雾浓度："+ BigDecimalUtils.roundToOneDecimal(records.get(records.size()-1).getAverageSmogLevel()));
                }catch (Exception e){
                    log.error("消息发送失败，用户ID: " + userId, e);
                }
            }

        }
    }
}
