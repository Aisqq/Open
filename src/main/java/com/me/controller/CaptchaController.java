package com.me.controller;

import com.me.dao.UserDao;
import com.me.service.CaptchaService;
import com.me.utils.Message;
import com.me.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    private final CaptchaService captchaService;
    private final UserDao userDao;
    /**
     * 发送登录图片验证码
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/pic")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("他");
        captchaService.generateCaptcha(request,response);
    }

    @PostMapping("/sendCode")
    public Result<String> sendCaptcha(@RequestBody Map<String,String> map){
        log.info(map.get("phone"));
        if(userDao.findUserByPhone(map.get("phone"))==null){
            return Result.error(Message.NOT_EXIST);
        }
        return captchaService.sendCaptcha(map.get("phone"));
    }
}
