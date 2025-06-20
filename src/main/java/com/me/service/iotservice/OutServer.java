package com.me.service.iotservice;


import com.me.dao.OutDao;
import com.me.entity.Out;
import com.me.service.IotDeviceServer;
import com.me.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OutServer implements IotDeviceServer {
    private final OutDao outDao;
    @Override
    public boolean findDeviceType(String deviceType) {
        String type = "home";
        return type.equals(deviceType);
    }

    @Override
    public void addData(Map<String, Object> map) {
        Out out = new Out();
        out.setDeviceId((String) map.get("deviceId"));
        out.setOutTime(TimeUtil.stringToLocalDateTime((String) map.get("recordTime")));
        outDao.add(out);
        log.info("out"+out);
    }
}
