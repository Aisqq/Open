package com.me.service.iot;


import com.me.dao.OutDao;
import com.me.entity.Out;
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
public class OutServer implements IotDeviceServer {
    private final OutDao outDao;
    @Override
    public boolean findDeviceType(String deviceType) {
        String type = "out";
        return type.equals(deviceType);
    }

    @Override
    public void addData(Map<String, Object> map) {
        log.info("out");
        map.put("deviceId","107");
        if(map.containsKey("out")&&(int)map.get("out")==0)return;
        log.info(map.toString());
        Out out = new Out();
        out.setDeviceId((String) map.get("deviceId"));
        out.setOutTime(TimeUtil.stringToLocalDateTime((String) map.get("recordTime")));
        outDao.add(out);
    }
}
