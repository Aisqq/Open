package com.me.service.iotservice;

import com.me.dao.TempDao;
import com.me.entity.Temp;
import com.me.service.IotDeviceServer;
import com.me.utils.TimeUtil;
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

    @Override
    public boolean findDeviceType(String deviceType) {
        String type = "temp";
        return type.equals(deviceType);
    }
    /**
     * 先查找当天该设备有没有记录数据，有的话修改，否则添加
     * @param map
     */
    @Override
    public void addData(Map<String, Object> map) {
        Temp temp = new Temp();
        Object tempValue = map.get("temperature");
        BigDecimal temperature;
        if (tempValue instanceof Number) {
            temperature = new BigDecimal(tempValue.toString());
        } else if (tempValue instanceof String) {
            temperature
                    = new BigDecimal((String) tempValue);
        } else {
            temperature = BigDecimal.ZERO;
        }

        temp.setTemperature(temperature);
        temp.setDeviceId((String) map.get("deviceId"));
        temp.setRecordTime(TimeUtil.stringToLocalDateTime((String) map.get("recordTime")));
        LocalDateTime startOfDay = temp.getRecordTime().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = temp.getRecordTime().toLocalDate().atTime(LocalTime.MAX);
        String deviceId = temp.getDeviceId();
        Temp findTemp = tempDao.getTempByDeviceIdAndDate(deviceId,startOfDay,endOfDay);
        System.out.println(findTemp);
        if(findTemp!=null){
            temp.setTempId(findTemp.getTempId());
            tempDao.updateTempById(temp);
        }else {
            tempDao.addTempData(temp);
            log.info("添加温度数据："+temp);
        }

    }
}
