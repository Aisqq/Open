package com.me.service.iot;

import com.me.dao.SleepDao;
import com.me.entity.Sleep;
import com.me.service.IotDeviceServer;
import com.me.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class SleepServer implements IotDeviceServer {
    private final SleepDao sleepDao;

    @Override
    public boolean findDeviceType(String deviceType) {
        String type = "sleep";
        return type.equals(deviceType);
    }

    @Override
    public void addData(Map<String, Object> map) {
        log.info("sleep");
        map.put("deviceId","104");
        log.info(map.toString());
        Sleep sleep = new Sleep();
        sleep.setDeviceId((String) map.get("deviceId"));
        sleep.setTurnOverCount((int) Double.parseDouble(String.valueOf(map.get("sleep"))));
        sleep.setRecordTime(TimeUtil.stringToLocalDateTime((String) map.get("recordTime")));
        sleepDao.add(sleep);
    }
}
