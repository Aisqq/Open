package com.me.service.iotservice;
import com.me.dao.HomeDao;
import com.me.entity.Home;
import com.me.service.IotDeviceServer;
import com.me.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class HomeServer implements IotDeviceServer {
    private final HomeDao homeDao;
    @Override
    public boolean findDeviceType(String deviceType) {
        String type = "home";
        return type.equals(deviceType);
    }

    @Override
    public void addData(Map<String, Object> map) {
        Home home  = new Home();
        home.setDeviceId((String) map.get("deviceId"));
        home.setHomeTime(TimeUtil.stringToLocalDateTime((String) map.get("recordTime")));
        homeDao.add(home);
        log.info("home"+home);
    }
}
