package com.me.task;

import com.me.dao.DeviceDao;
import com.me.entity.Device;
import com.me.service.AnalyzeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
@Component
@Slf4j
@RequiredArgsConstructor
public class AnalyzeTask {
    private final List<String> targetDevices = Arrays.asList("温度传感器", "烟雾传感器");
    private final DeviceDao deviceDao;
    private final List<AnalyzeService> analyzeServices;

    /**
     * 每天22点分析今日所有数据情况
     * 1、查询所有设备
     * 2、根据设备类型查询对应的数据（策略模式）
     * 3、线程池优化加快分析
     */
    @Scheduled(cron = "0 0 22 * * ?")
    public void analyzeTask22() {
        LocalDateTime endDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime startDate = endDate.plusDays(-30);
        endDate = endDate.plusDays(1);
        int numDays = (int) ChronoUnit.DAYS.between(startDate, endDate);
        double[] data = new double[numDays];
        Arrays.fill(data, Double.NaN);
        List<Device> devices = deviceDao.findAll();
        for (Device device : devices) {
            analyzeDeviceAsync(device, startDate, endDate, numDays, data);
        }
    }

    @Async
    public void analyzeDeviceAsync(Device device, LocalDateTime startDate, LocalDateTime endDate, int numDays, double[] data) {
        for (AnalyzeService analyzeService : analyzeServices) {
            Arrays.fill(data, Double.NaN);
            if (analyzeService.findDeviceType(device.getDeviceName())) {
                analyzeService.analyze(device, startDate, endDate, numDays, data);
                break;
            }
        }
    }

    /**
     * 每小时分析温度和烟雾浓度
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void analyzeTask() {
        LocalDateTime endDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime startDate = endDate.plusDays(-30);
        endDate = endDate.plusDays(1);
        int numDays = (int) ChronoUnit.DAYS.between(startDate, endDate);
        double[] data = new double[numDays];
        Arrays.fill(data, Double.NaN);
        List<Device> devices = deviceDao.findAll();
        for (Device device : devices) {
            if(targetDevices.contains(device.getDeviceName()))
            analyzeDeviceAsync(device, startDate, endDate, numDays, data);
        }
    }
}
