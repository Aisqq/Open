package com.me.controller;

import com.me.service.CaptchaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    private final CaptchaService captchaService;

    /**
     * 发送登录图片验证码
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/pic")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        captchaService.generateCaptcha(request,response);
    }


}
