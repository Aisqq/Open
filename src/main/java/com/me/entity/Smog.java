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
    private Integer deviceId;
    private BigDecimal smogLever;
    private Boolean alarm;
    private LocalDateTime recordTime;
}
