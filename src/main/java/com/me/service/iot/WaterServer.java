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
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        Object value = map.get("water");
        if (value instanceof String) {
            water.setWaterUsage(new BigDecimal((String)value));
        } else {
            water.setWaterUsage((BigDecimal) value);
        }
        if (water.getWaterUsage().compareTo(BigDecimal.ZERO) <= 0) return;

        water.setDeviceId((String) map.get("deviceId"));
        water.setRecordTime(TimeUtil.stringToLocalDateTime((String) map.get("recordTime")));

        LocalDateTime startOfDay = water.getRecordTime().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = water.getRecordTime().toLocalDate().atTime(LocalTime.MAX);
        String deviceId = water.getDeviceId();

        Water existingWater = waterDao.getWaterByDeviceIdAndDate(deviceId, startOfDay, endOfDay);
        if (existingWater != null) {
            water.setWaterId(existingWater.getWaterId());
            waterDao.updateWaterById(water);
            log.info("更新用水数据: {}", water);
        } else {
            waterDao.add(water);
            log.info("添加用水数据: {}", water);
        }
    }
}
