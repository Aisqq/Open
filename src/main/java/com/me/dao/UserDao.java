package com.me.dao;

import com.me.entity.User;
import org.apache.ibatis.annotations.Select;

public interface UserDao {
    @Select("select * from tb_user where username = #{username}")
    User findUserByUsername(String username);
    @Select("select * from tb_user where phone = #{phone}")
    User findUserByPhone(String username);
}
