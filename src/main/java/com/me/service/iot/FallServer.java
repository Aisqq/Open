package com.me.service.iot;


import com.me.dao.AlarmLogDao;
import com.me.dao.DeviceDao;
import com.me.dao.UserDao;
import com.me.entity.AlarmLog;
import com.me.service.IotDeviceServer;
import com.me.utils.SseSendUtil;
import com.me.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FallServer implements IotDeviceServer {
    private final String reason = "老人摔倒了";
    private final StringRedisTemplate redisTemplate;
    private final UserDao userDao;
    private final DeviceDao deviceDao;
    private final AlarmLogDao alarmLogDao;
    @Override
    public boolean findDeviceType(String deviceType) {
        String type = "fall";
        return type.equals(deviceType);
    }

    @Override
    public void addData(Map<String, Object> map) {
        String deviceId = "100";
        log.info(map.toString());
        if(map.get("fall")!=null){
           Integer elderId = deviceDao.findById(deviceId).getElderId();
           List<Integer> userIds = userDao.findUserIdByElderId(elderId);
           for(Integer userId:userIds){
               try {
                   SseSendUtil.SseSend(userId,reason);
               }catch (Exception e){
                   log.info(e.getMessage());
               }
           }
           AlarmLog alarmLog = new AlarmLog();
           alarmLog.setReason(reason);
           alarmLog.setAlarmType("摔倒检测");
           alarmLog.setAlarmTime(TimeUtil.stringToLocalDateTime((String) map.get("recordTime")));
           alarmLog.setElderId(elderId);
           alarmLogDao.add(alarmLog);
        }
    }
}
