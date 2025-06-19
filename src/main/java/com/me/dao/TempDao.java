package com.me.dao;

import com.me.entity.Temp;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

public interface TempDao {

    @Insert("INSERT INTO tb_temp (device_id, temperature, record_time) " +
            "VALUES (#{deviceId}, #{temperature}, #{recordTime})")
    @Options(useGeneratedKeys = true, keyProperty = "tempId", keyColumn = "temp_id")
    void addTempData(Temp temp);


    @Update("UPDATE tb_temp " +
            "SET temperature = #{temperature}, " +
            "record_time = #{recordTime} " +
            "WHERE temp_id = #{tempId}")
    void updateTempById(Temp temp);


    @Select("SELECT * FROM tb_temp " +
            "WHERE device_id = #{deviceId} " +
            "AND record_time BETWEEN #{startOfDay} AND #{endOfDay} " +
            "LIMIT 1")
    Temp getTempByDeviceIdAndDate(
            @Param("deviceId") String deviceId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime
                    endOfDay
    );
}
