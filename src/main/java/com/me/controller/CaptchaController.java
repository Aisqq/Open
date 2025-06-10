package com.me.controller;

import com.me.service.CaptchaService;
import com.me.utils.Message;
import com.me.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

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

    @PostMapping("/sendCaptcha")
    public Result<String> sendCaptcha(@RequestBody Map<String,String> map){
        if(map.get("phone")==null||map.get("phone").length()!=6){
            return Result.error(Message.ERROR);
        }
        return captchaService.sendCaptcha(map.get("phone"));
    }
}
