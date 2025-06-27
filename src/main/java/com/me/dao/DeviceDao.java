package com.me.dao;

import com.me.entity.Device;

import java.util.List;

public interface DeviceDao {

    void batchInsertDevice(List<Device> deviceList);

    List<Device> findByElderId(Integer id);

    Device findById(String deviceId);
}
