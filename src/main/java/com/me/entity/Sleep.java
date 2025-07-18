package com.me.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sleep {
    private Integer sleepId;
    private String  deviceId;
    private Integer turnOverCount;
    private LocalDateTime recordTime;
}
