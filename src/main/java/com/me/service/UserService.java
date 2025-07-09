package com.me.service;

import com.me.vo.ElderVo;
import com.me.vo.OutStatusVO;
import com.me.dto.RegisterDTO;
import com.me.entity.User;
import com.me.utils.Result;
import com.me.vo.UserVo;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface UserService {

    Result<String> login(User userLogin, HttpServletResponse response);

    Result<String> register(RegisterDTO dto);

    Result<String> setPassword(String password);

    Result<String> resetPassword(String phone,String code,HttpServletResponse response);

    Result<BigDecimal> getTemp(LocalDateTime date);

    Result<BigDecimal> getWaterUsage( LocalDateTime date);

    Result<OutStatusVO> getMovementStats(LocalDateTime date);

    Result<Integer> getTurnOverCount(LocalDateTime date);

    Result<BigDecimal> getSmog(LocalDateTime date);

    Result<ElderVo> getElder();

    Result<UserVo> getUser();

    Result<String> edit(User user,HttpServletResponse response);

    Result<Integer> getFall(LocalDateTime date);
}
