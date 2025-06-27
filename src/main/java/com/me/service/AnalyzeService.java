package com.me.service;

import com.me.dao.ElderDao;
import com.me.vo.record.TemperatureRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static com.me.utils.ModelUtils.analyseFunction;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyzeService {
    private final ElderDao elderDao;
    public double analyze(String deviceId) {
        Integer elderId = 1;
        LocalDateTime endDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime startDate = endDate.plusDays(-30);
        endDate = endDate.plusDays(1);
        List<TemperatureRecord> records = elderDao.getTemperatureValueRange(elderId, startDate, endDate);
        for(TemperatureRecord x:records){
            System.out.println(x);
        }
        int numDays = (int) ChronoUnit.DAYS.between(startDate, endDate);
        double[] data = new double[numDays];
        Arrays.fill(data,Double.NaN);
        for (TemperatureRecord record : records) {
            int dayIndex = (int) ChronoUnit.DAYS.between(startDate, record.getRecordTime());
            if (dayIndex >= 0 && dayIndex < numDays) {
                data[dayIndex] = record.getTemperature().doubleValue();
            }
        }
        for(int i = 0;i<data.length;i++){
            System.out.println(data[i]);
        }
        double[] scoreResult = analyseFunction(data,0.3);
        return scoreResult[1];
    }
}
