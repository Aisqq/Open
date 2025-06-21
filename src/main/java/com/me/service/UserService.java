package com.me.service;

import com.me.vo.OutStatusVO;
import com.me.dto.RegisterDTO;
import com.me.entity.User;
import com.me.utils.Result;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;

public interface UserService {
    Result<String> login(User userLogin, HttpServletResponse response);

    Result<String> register(RegisterDTO dto);

    Result<String> setPassword(String password);

    Result<String> resetPassword(String phone,String code,HttpServletResponse response);

    Result<BigDecimal> getTemp();

    Result<BigDecimal> getWaterUsage();

    Result<OutStatusVO> getMovementStats();

    Result<Integer> getTurnOverCount();
}
