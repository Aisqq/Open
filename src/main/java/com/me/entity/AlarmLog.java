package com.me.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmLog {
    private Integer alarmId;
    private String alarmType;
    private String reason;
    private LocalDateTime alarmTime;
}
