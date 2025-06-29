package com.me.service;

import com.me.entity.Device;

import java.time.LocalDateTime;

public interface AnalyzeService {
    boolean findDeviceType(String deviceName);


    void analyze(Device device, LocalDateTime startDate, LocalDateTime endDate, int numDays, double[] data);
}
