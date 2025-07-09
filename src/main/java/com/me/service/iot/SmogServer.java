package com.me.service.iot;

import com.me.dao.SmogDao;
import com.me.entity.Smog;
import com.me.service.IotDeviceServer;
import com.me.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Service
@Transactional
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
        map.put("deviceId","222");
        log.info(map.toString());
        Smog smog = new Smog();
        smog.setSmogLever(new BigDecimal((String) map.get("smog")));
        smog.setRecordTime(TimeUtil.stringToLocalDateTime((String) map.get("recordTime")));
        smog.setDeviceId((String) map.get("deviceId"));
        //smog.setAlarm((Integer) map.get("alarm"));
        if(smog.getSmogLever().compareTo(new BigDecimal(100))<=0)
            smog.setAlarm(0);
        else
            smog.setAlarm(1);
        LocalDateTime startOfDay = smog.getRecordTime().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = smog.getRecordTime().toLocalDate().atTime(LocalTime.MAX);
        Smog findSmog = smogDao.getLatestByDeviceIdAndDate(smog.getDeviceId(),startOfDay,endOfDay);
        if(findSmog==null){
            smogDao.add(smog);
        }else {
            smog.setSmogId(findSmog.getSmogId());
            if(findSmog.getAlarm()!=null)smog.setAlarm(findSmog.getAlarm());
            smogDao.updateById(smog);
        }
        log.info("烟雾数据:"+smog);
    }
}
