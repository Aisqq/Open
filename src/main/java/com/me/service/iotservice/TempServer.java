package com.me.service.iotservice;

import com.me.dao.TempDao;
import com.me.entity.Temp;
import com.me.service.IotDeviceServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class TempServer implements IotDeviceServer {
    private final TempDao tempDao;
    private final String type = "temp";
    @Override
    public boolean findDeviceType(String deviceType) {
        return type.equals(deviceType);
    }

    /**
     * 先查找当天该设备有没有记录数据，有的话修改，否则添加
     * @param map
     */
    @Override
    public void addData(Map<String, Object> map) {
        Temp temp = new Temp();
        temp.setTemperature((BigDecimal) map.get("temperature"));
        temp.setDeviceId((String) map.get("deviceId"));
        temp.setRecordTime((LocalDateTime) map.get("recordTime"));
        LocalDateTime startOfDay = temp.getRecordTime().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = temp.getRecordTime().toLocalDate().atTime(LocalTime.MAX);
        String deviceId = temp.getDeviceId();
        Temp findTemp = tempDao.getTempByDeviceIdAndDate(deviceId,startOfDay,endOfDay);
        if(findTemp!=null){
            tempDao.updateTempById(temp);
        }{
            tempDao.addTempData(temp);
            log.info("添加温度数据："+temp);
        }

    }
}
