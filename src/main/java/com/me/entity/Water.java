package com.me.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Water {
    private Integer waterId;
    private String  deviceId;
    private BigDecimal waterUsage;
    private LocalDateTime recordTime;
}
