package com.me.vo.record;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TurnOverRecord {
    private LocalDateTime date;
    private Integer turnOverCount;
}
