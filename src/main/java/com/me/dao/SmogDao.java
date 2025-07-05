package com.me.dao;

import com.me.entity.Smog;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

public interface SmogDao {

    @Insert("INSERT INTO tb_smog (device_id, smog_level, is_alarm, record_time) " +
            "VALUES (#{deviceId}, #{smogLever}, #{alarm}, #{recordTime})")
    @Options(useGeneratedKeys = true, keyProperty = "smogId", keyColumn = "smog_id")
    void add(Smog smog);

    @Update("UPDATE tb_smog " +
            "SET smog_level = #{smogLever}, " +
            "is_alarm = #{alarm}, " +
            "record_time = #{recordTime} " +
            "WHERE smog_id = #{smogId}")
    void updateById(Smog smog);

    @Select("SELECT * FROM tb_smog " +
            "WHERE device_id = #{deviceId} " +
            "AND record_time BETWEEN #{startOfDay} AND #{endOfDay} " +
            "ORDER BY record_time DESC " +
            "LIMIT 1")
    Smog getLatestByDeviceIdAndDate(
            @Param("deviceId") String deviceId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}
