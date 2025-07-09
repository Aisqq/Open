package com.me.service;

import java.util.Map;

public interface IotDeviceServer {

    boolean findDeviceType(String deviceType);

    void addData(Map<String,Object> map);
}
