package com.me.vo.record;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TemperatureRecord {
    private LocalDateTime recordTime;
    private BigDecimal temperature;
}
