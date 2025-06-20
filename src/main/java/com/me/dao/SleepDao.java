package com.me.dao;

import com.me.entity.Sleep;
import org.apache.ibatis.annotations.*;


public interface SleepDao {

    @Insert("INSERT INTO tb_sleep (device_id, turn_over_count, record_date) " +
            "VALUES (#{deviceId}, #{turnOverCount}, #{recordTime})")
    @Options(useGeneratedKeys = true, keyProperty = "sleepId", keyColumn = "sleep_id")
    void add(Sleep sleep);

}

