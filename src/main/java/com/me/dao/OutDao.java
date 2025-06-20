package com.me.dao;

import com.me.entity.Out;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface OutDao {
    @Insert("INSERT INTO tb_out (device_id, out_time) " +
            "VALUES (#{deviceId},  #{outTime})")
    @Options(useGeneratedKeys = true, keyProperty = "outId", keyColumn = "out_id")
    void add(Out out);
}
