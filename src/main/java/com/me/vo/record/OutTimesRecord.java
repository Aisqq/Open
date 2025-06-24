package com.me.vo.record;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OutTimesRecord {
    private LocalDateTime date;
    private Integer outTimes;
}
