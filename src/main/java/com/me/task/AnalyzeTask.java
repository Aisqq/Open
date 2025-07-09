package com.me.task;

import com.me.dao.DeviceDao;
import com.me.entity.Device;
import com.me.service.task.AnalyzeTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final AnalyzeTaskService analyzeService;

    /**
     * 每天22点分析今日所有数据情况
     * 1、查询所有设备
     * 2、根据设备类型查询对应的数据（策略模式）
     * 3、线程池优化加快分析
     */
    @Async("taskExecutor")
    @Scheduled(cron = "0 0 22 * * ?")
    public void analyzeTask22() {
        LocalDateTime endDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime startDate = endDate.plusDays(-30);
        endDate = endDate.plusDays(1);
        int numDays = (int) ChronoUnit.DAYS.between(startDate, endDate);
        List<Device> devices = deviceDao.findAll();
        for (Device device : devices) {
            analyzeService.analyzeDeviceAsync(device, startDate, endDate, numDays);
        }
    }

    /**
     * 每小时分析温度和烟雾浓度
     */
    @Async("taskExecutor")
    //@Scheduled(cron = "0 * * * * ?")//测试1分钟
    @Scheduled(cron = "0 0 * * * ?")
    public void analyzeTask() {
        log.info("定时任务");
        LocalDateTime endDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime startDate = endDate.plusDays(-30);
        endDate = endDate.plusDays(1);
        int numDays = (int) ChronoUnit.DAYS.between(startDate, endDate);
        List<Device> devices = deviceDao.findAll();
        for (Device device : devices) {
            if(targetDevices.contains(device.getDeviceName()))
                analyzeService.analyzeDeviceAsync(device, startDate, endDate, numDays);
        }
    }
}
