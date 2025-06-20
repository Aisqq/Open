package com.me.dao;

import com.me.entity.Water;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface WaterDao {

    @Insert("INSERT INTO tb_water (device_id, waterUsage, record_time) " +
            "VALUES (#{deviceId}, #{water_usage}, #{recordTime})")
    @Options(useGeneratedKeys = true, keyProperty = "waterId", keyColumn = "water_id")
    void add(Water water);
}
