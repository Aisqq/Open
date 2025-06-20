package com.me.service.iotservice;

import com.me.service.IotDeviceServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class WaterServer implements IotDeviceServer {
    @Override
    public boolean findDeviceType(String deviceType) {
        String type = "water";
        return type.equals(deviceType);
    }

    @Override
    public void addData(Map<String, Object> map) {

    }
}
