package com.me.dao;

import com.me.entity.Elder;
import org.apache.ibatis.annotations.Select;

public interface ElderDao {

    @Select("select * from tb_elder where secret_key = #{key}")
    Elder findByKey(String key);
}
