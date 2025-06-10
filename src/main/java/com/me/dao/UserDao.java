package com.me.dao;

import com.me.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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


    @Update({
            "<script>",
            "UPDATE \"tb_user\"",
            "<set>",
            "<if test='username != null'>username = #{username},</if>",
            "<if test='password != null'>password = #{password},</if>",
            "<if test='phone != null'>phone = #{phone},</if>",
            "<if test='elderId != null'>elder_id = #{elderId},</if>",
            "</set>",
            "WHERE user_id = #{userId}",
            "</script>"
    })
    void update(User user);
}
