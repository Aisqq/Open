package com.me.service;

import com.me.dao.ElderDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@RequiredArgsConstructor
@Slf4j
@Service
public class ToolsService {
    private final ElderDao elderDao;
    @Tool(description = "获取当前时间或日期")
    public String getDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "当前时间：" + now.format(formatter) + "。";
    }

    @Tool(description = "获取某一天老人的情况，默认当天")
    public String getCondition(@ToolParam(description = "日期格式，例如：2023-10-01,如果用户没有提供就获取当天的日期,可以先调用getDate方法") String dateStr,@ToolParam(description = "老人的Id")Integer elderId) {
        log.info("elderID:"+elderId+"date"+dateStr);
        LocalDateTime date;
        if (dateStr == null || dateStr.isEmpty()) {
            date = LocalDateTime.now();
        } else {
            try {
                LocalDate localDate = LocalDate.parse(dateStr);
                date = localDate.atStartOfDay();
            } catch (Exception e) {
                return "日期格式错误，请使用 yyyy-MM-dd 格式";
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("date", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        BigDecimal waterUsage = Optional.ofNullable(elderDao.findWaterUsage(elderId, date))
                .orElse(BigDecimal.ZERO);
        BigDecimal temperature = Optional.ofNullable(elderDao.getLatestTemperatureValue(elderId, date))
                .orElse(BigDecimal.ZERO);
        BigDecimal smogLevel = Optional.ofNullable(elderDao.getAverageSmogLevel(elderId, date))
                .orElse(BigDecimal.ZERO);
        Integer homeTimes = Optional.ofNullable(elderDao.countHomeTimes(elderId, date))
                .orElse(0);
        Integer outTimes = Optional.ofNullable(elderDao.countOutTimes(elderId, date))
                .orElse(0);

        resultMap.put("waterUsage", waterUsage);
        resultMap.put("temperature", temperature);
        resultMap.put("smogLevel", smogLevel);
        resultMap.put("homeTimes", homeTimes);
        resultMap.put("outTimes", outTimes);

        return resultMap.toString();
    }
}