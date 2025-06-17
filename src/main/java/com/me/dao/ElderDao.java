package com.me.dao;

import com.github.pagehelper.Page;
import com.me.entity.Elder;
import com.me.vo.ElderVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ElderDao {

    @Select("select * from tb_elder where secret_key = #{key}")
    Elder findByKey(String key);

    @Insert("INSERT INTO tb_elder (name, gender, age, secret_key) VALUES (#{name}, #{gender}, #{age}, #{secret_key})")
    @Options(useGeneratedKeys = true, keyProperty = "elderId")
    void addElder(Elder elder);

    Page<ElderVo> findByCondition(String query);

    int update(Elder elder);


    BigDecimal findWaterUsage(@Param("elderId") Integer elderId, @Param("date") LocalDateTime date);

    BigDecimal getLatestTemperatureValue(@Param("elderId") Integer elderId, @Param("date") LocalDateTime date);

    Integer countHomeTimes(@Param("elderId") Integer elderId, @Param("date") LocalDateTime date);

    Integer countOutTimes(@Param("elderId") Integer elderId, @Param("date") LocalDateTime date);

    BigDecimal getAverageSmogLevel(@Param("elderId") Integer elderId, @Param("date") LocalDateTime date);
}
