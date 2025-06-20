package com.me.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Smog {
    private Integer smogId;
    private String  deviceId;
    private BigDecimal smogLever;
    private Integer alarm;
    private LocalDateTime recordTime;
}
