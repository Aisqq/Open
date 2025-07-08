package com.me.service.task;

import com.me.entity.Device;
import com.me.service.AnalyzeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyzeTaskService {
    private final List<AnalyzeService> analyzeServices;
    @Async("taskExecutor")
    public void analyzeDeviceAsync(Device device, LocalDateTime startDate, LocalDateTime endDate, int numDays) {
        double[] data = new double[numDays];
        Arrays.fill(data, Double.NaN);
        log.info("异步线程");
        for (AnalyzeService analyzeService : analyzeServices) {
            if (analyzeService.findDeviceType(device.getDeviceName())) {
                analyzeService.analyze(device, startDate, endDate, numDays, data);
                break;
            }
        }
    }
}
