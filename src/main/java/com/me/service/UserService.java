package com.me.service;

import com.me.dto.RegisterDTO;
import com.me.entity.User;
import com.me.utils.Result;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    Result<String> login(User userLogin, HttpServletResponse response);

    Result<String> register(RegisterDTO dto);
}
