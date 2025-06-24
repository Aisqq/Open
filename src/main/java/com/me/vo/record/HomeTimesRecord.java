package com.me.vo.record;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HomeTimesRecord {
    private LocalDateTime date;
    private Integer homeTimes;
}
