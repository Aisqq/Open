package com.me.dao;

import com.github.pagehelper.Page;
import com.me.entity.Elder;
import com.me.vo.ElderVo;
import com.me.vo.record.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ElderDao {

    @Select("select * from tb_elder where secret_key = #{key}")
    Elder findByKey(String key);

    @Insert("INSERT INTO tb_elder (name, gender, age, secret_key) VALUES (#{name}, #{gender}, #{age}, #{secretKey})")
    @Options(useGeneratedKeys = true, keyProperty = "elderId")
    void addElder(Elder elder);

    Page<ElderVo> findByCondition(String query);

    int update(Elder elder);
    @Select("select * from tb_elder where elder_id = #{elderId}")
    Elder findById(Integer elderId);

    BigDecimal findWaterUsage(@Param("elderId") Integer elderId, @Param("date") LocalDateTime date);

    BigDecimal getLatestTemperatureValue(@Param("elderId") Integer elderId, @Param("date") LocalDateTime date);

    Integer countHomeTimes(@Param("elderId") Integer elderId, @Param("date") LocalDateTime date);

    Integer countOutTimes(@Param("elderId") Integer elderId, @Param("date") LocalDateTime date);

    BigDecimal getAverageSmogLevel(@Param("elderId") Integer elderId, @Param("date") LocalDateTime date);

    Integer turnOverCount(@Param("elderId") Integer elderId, @Param("date") LocalDateTime date);


    /**
     * 查询老年人在指定时间范围内的用水量记录
     * @param elderId 老年人ID
     * @param startDate 开始日期（包含）
     * @param endDate 结束日期（包含）
     * @return 用水量记录列表
     */
    List<WaterUsageRecord> findWaterUsageRange(
            @Param("elderId") Integer elderId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * 查询老年人在指定时间范围内的体温记录
     * @param elderId 老年人ID
     * @param startDate 开始日期（包含）
     * @param endDate 结束日期（包含）
     * @return 体温记录列表
     */
    List<TemperatureRecord> getTemperatureValueRange(
            @Param("elderId") Integer elderId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * 查询老年人在指定时间范围内的回家次数统计
     * @param elderId 老年人ID
     * @param startDate 开始日期（包含）
     * @param endDate 结束日期（包含）
     * @return 回家次数统计列表（按天）
     */
    List<HomeTimesRecord> countHomeTimesRange(
            @Param("elderId") Integer elderId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * 查询老年人在指定时间范围内的外出次数统计
     * @param elderId 老年人ID
     * @param startDate 开始日期（包含）
     * @param endDate 结束日期（包含）
     * @return 外出次数统计列表（按天）
     */
    List<OutTimesRecord> countOutTimesRange(
            @Param("elderId") Integer elderId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * 查询老年人在指定时间范围内的平均雾霾水平
     * @param elderId 老年人ID
     * @param startDate 开始日期（包含）
     * @param endDate 结束日期（包含）
     * @return 平均雾霾水平列表（按天）
     */
    List<SmogLevelRecord> getAverageSmogLevelRange(
            @Param("elderId") Integer elderId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * 查询老年人在指定时间范围内的翻身次数统计
     * @param elderId 老年人ID
     * @param startDate 开始日期（包含）
     * @param endDate 结束日期（包含）
     * @return 翻身次数统计列表（按天）
     */
    List<TurnOverRecord> turnOverCountRange(
            @Param("elderId") Integer elderId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);


}
