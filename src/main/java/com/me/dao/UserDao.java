package com.me.dao;

import com.me.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface UserDao {
    @Select("select * from tb_user where username = #{username}")
    User findUserByUsername(String username);
    @Select("select * from tb_user where phone = #{phone}")
    User findUserByPhone(String username);


    @Insert("INSERT INTO tb_user (" +
            "username, password, phone, elder_id) " +
            "VALUES (" +
            "#{username}, #{password}, #{phone}, #{elderId})"
    )
    void save(User user);

    void update(User user);
}
