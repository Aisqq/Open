package com.me.vo.record;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WaterUsageRecord {
    private LocalDateTime recordDate;
    private BigDecimal waterUsage;
}
