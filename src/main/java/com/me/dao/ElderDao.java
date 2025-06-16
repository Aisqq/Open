package com.me.dao;

import com.github.pagehelper.Page;
import com.me.entity.Elder;
import com.me.vo.ElderVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

public interface ElderDao {

    @Select("select * from tb_elder where secret_key = #{key}")
    Elder findByKey(String key);

    @Insert("INSERT INTO tb_elder (name, gender, age, secret_key) VALUES (#{name}, #{gender}, #{age}, #{secret_key})")
    @Options(useGeneratedKeys = true, keyProperty = "elderId")
    void addElder(Elder elder);

    Page<ElderVo> findByCondition(String query);

    int update(Elder elder);
}
