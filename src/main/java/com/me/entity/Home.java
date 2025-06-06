package com.me.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Home {
    private Integer homeId;
    private Integer deviceId;
    private LocalDateTime homeTime;
}
