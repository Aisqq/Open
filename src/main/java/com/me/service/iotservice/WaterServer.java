package com.me.service.iotservice;

import com.me.dao.WaterDao;
import com.me.entity.Water;
import com.me.service.IotDeviceServer;
import com.me.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class WaterServer implements IotDeviceServer {
    private final WaterDao waterDao;
    @Override
    public boolean findDeviceType(String deviceType) {
        String type = "water";
        return type.equals(deviceType);
    }

    @Override
    public void addData(Map<String, Object> map) {
        Water water = new Water();
        water.setWaterUsage((BigDecimal)map.get("water"));
        if (water.getWaterUsage().compareTo(BigDecimal.ZERO) <= 0) return;
        water.setDeviceId((String) map.get("deviceId"));
        water.setRecordTime(TimeUtil.stringToLocalDateTime((String) map.get("recordTime")));
        waterDao.add(water);
        log.info("添加用水数据"+water);
    }
}
