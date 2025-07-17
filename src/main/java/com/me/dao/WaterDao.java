package com.me.dao;

import com.me.entity.Water;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

public interface WaterDao {

    @Insert("INSERT INTO tb_water (device_id, water_usage, record_time) " +
            "VALUES (#{deviceId}, #{waterUsage}, #{recordTime})")
    @Options(useGeneratedKeys = true, keyProperty = "waterId", keyColumn = "water_id")
    void add(Water water);

    @Select("SELECT * FROM tb_water " +
            "WHERE device_id = #{deviceId} " +
            "AND record_time BETWEEN #{startTime} AND #{endTime} " +
            "ORDER BY record_time DESC " +
            "LIMIT 1")
    Water getWaterByDeviceIdAndDate(String deviceId, LocalDateTime startTime, LocalDateTime endTime);

    @Update("UPDATE tb_water " +
            "SET water_usage = #{waterUsage}, " +
            "record_time = #{recordTime} " +
            "WHERE water_id = #{waterId}")
    void updateWaterById(Water water);
}
