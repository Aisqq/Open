package com.me.service.iot;
import com.me.dao.HomeDao;
import com.me.entity.Home;
import com.me.service.IotDeviceServer;
import com.me.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Slf4j
@Transactional
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
        log.info("home");
        map.put("deviceId","101");
        log.info(map.toString());
        if(map.containsKey("home")&&(int)map.get("home")==0)return;
        Home home = new Home();
        home.setDeviceId((String) map.get("deviceId"));
        home.setHomeTime(TimeUtil.stringToLocalDateTime((String) map.get("recordTime")));
        homeDao.add(home);
    }
}
