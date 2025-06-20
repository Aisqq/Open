package com.me.service.iotservice;

import com.me.dao.SmogDao;
import com.me.entity.Smog;
import com.me.service.IotDeviceServer;
import com.me.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmogServer implements IotDeviceServer {
    private final SmogDao smogDao;
    @Override
    public boolean findDeviceType(String deviceType) {
        String type = "smog";
        return type.equals(deviceType);
    }

    /**
     * 查询当天是否记录
     * 否添加
     * 是修改
     * 报警次数不修改
     * @param map
     */
    @Override
    public void addData(Map<String, Object> map) {
        Smog smog = new Smog();
        smog.setSmogLever((BigDecimal) map.get("smog"));
        smog.setRecordTime(TimeUtil.stringToLocalDateTime((String) map.get("recodeTime")));
        smog.setDeviceId((String) map.get("deviceId"));
        smog.setAlarm((Integer) map.get("alarm"));
        LocalDateTime startOfDay = smog.getRecordTime().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = smog.getRecordTime().toLocalDate().atTime(LocalTime.MAX);
        Smog findSmog = smogDao.getLatestByDeviceIdAndDate(smog.getDeviceId(),startOfDay,endOfDay);
        if(findSmog==null){
            smogDao.add(smog);
        }else {
            smog.setSmogId(findSmog.getSmogId());
            if(findSmog.getAlarm()!=0)smog.setAlarm(findSmog.getAlarm());
            smogDao.updateById(smog);
        }
        log.info("烟雾数据:"+smog);
    }
}
