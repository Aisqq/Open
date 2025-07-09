package com.me.service;

import com.me.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface CaptchaService {

    void generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException;

    Result<String> sendCaptcha(String phone);
}
