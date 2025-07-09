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
import com.me.vo.record.OutTimesRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutAnalyze implements AnalyzeService {
    private final ElderDao elderDao;
    private final AlarmLogDao alarmLogDao;
    private final String outName = Message.OUT_NAME;
    private final UserDao userDao;
    @Override
    public boolean findDeviceType(String deviceName) {
        return outName.equals(deviceName);
    }

    @Override
    public void analyze(Device device, LocalDateTime startDate, LocalDateTime endDate, int numDays, double[] data) {
        log.info("任务外出");
        List<OutTimesRecord> records = elderDao.countOutTimesRange(device.getElderId(), startDate, endDate);
        for (OutTimesRecord record : records) {
            int dayIndex = (int) ChronoUnit.DAYS.between(startDate, record.getDate());
            if (dayIndex >= 0 && dayIndex < numDays) {
                data[dayIndex] = record.getOutTimes().doubleValue();
            }
        }
        double[] scoreResult = ModelUtils.analyseFunction(data,0.3);
        if(scoreResult[0]==1){
            log.info("异常数据");
            AlarmLog alarmLog = new AlarmLog();
            alarmLog.setElderId(device.getElderId());
            alarmLog.setAlarmType(outName);
            alarmLog.setReason("老人数据异常，外出次数："+records.get(records.size()-1).getOutTimes());
            alarmLogDao.add(alarmLog);
            List<Integer> userIdList = userDao.findUserIdByElderId(device.getElderId());
            for(Integer userId:userIdList){
                try {
                    SseSendUtil.SseSend(userId,"老人数据异常，外出次数："+records.get(records.size()-1).getOutTimes());
                }catch (Exception e){
                    log.error("消息发送失败，用户ID: " + userId, e);
                }
            }
        }
    }
}
