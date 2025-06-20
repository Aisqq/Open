package com.me.dao;

import com.me.entity.Home;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface HomeDao {
    @Insert("INSERT INTO tb_home (device_id, home_time) " +
            "VALUES (#{deviceId},  #{homeTime})")
    @Options(useGeneratedKeys = true, keyProperty = "homeId", keyColumn = "home_id")
    void add(Home home);
}
