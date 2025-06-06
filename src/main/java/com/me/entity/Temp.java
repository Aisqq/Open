package com.me.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Temp {
    private Integer tempId;
    private Integer deviceId;
    private BigDecimal temperature;
    private LocalDateTime recordTime;
}
