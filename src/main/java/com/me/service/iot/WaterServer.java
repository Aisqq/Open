package com.me.service.iot;

import com.me.dao.WaterDao;
import com.me.entity.Water;
import com.me.service.IotDeviceServer;
import com.me.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class WaterServer implements IotDeviceServer {
    private final WaterDao waterDao;
    @Override
    public boolean findDeviceType(String deviceType) {
        String type = "water";
        return type.equals(deviceType);
    }

    @Override
    public void addData(Map<String, Object> map) {
        map.put("deviceId","105");
        log.info(map.toString());
        Water water = new Water();
        water.setWaterUsage(new BigDecimal((String)map.get("water")));
        if (water.getWaterUsage().compareTo(BigDecimal.ZERO) <= 0) return;
        water.setDeviceId((String) map.get("deviceId"));
        water.setRecordTime(TimeUtil.stringToLocalDateTime((String) map.get("recordTime")));
        waterDao.add(water);
        log.info("添加用水数据"+water);
    }
}
