package com.me.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    private Integer deviceId;
    private Integer elderId;
    private String deviceName;
    private String deviceTagId;
}
