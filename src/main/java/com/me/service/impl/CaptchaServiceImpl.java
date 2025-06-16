package com.me.service.impl;

import com.me.dao.UserDao;
import com.me.service.CaptchaService;
import com.me.utils.CaptchaUtil;
import com.me.utils.Message;
import com.me.utils.RedisKey;
import com.me.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {
    private static final long CAPTCHA_EXPIRE_TIME = 3;
    private final StringRedisTemplate stringRedisTemplate;


    @Override
    public void generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 生成随机验证码
        String captchaText = CaptchaUtil.generateRandomText(4);
        String sessionId = request.getSession().getId();
        String redisKey = RedisKey.CAPTCHA + sessionId;
        stringRedisTemplate.opsForValue().set(
                redisKey,
                captchaText,
                CAPTCHA_EXPIRE_TIME,
                TimeUnit.MINUTES
        );
        // 生成图片并返回
        BufferedImage image = CaptchaUtil.generateCaptchaImage(captchaText, 120, 40);
        response.setContentType("image/png");
        ImageIO.write(image, "png", response.getOutputStream());
    }

    @Override
    public Result<String> sendCaptcha(String phone) {
       String code = CaptchaUtil.generateRandomCode(6);
        if(Boolean.FALSE.equals(stringRedisTemplate.opsForValue().setIfAbsent(RedisKey.FORGET_CAPTCHA + phone, code, 60, TimeUnit.SECONDS))){
            return Result.error(Message.SEND_ERROR);
        }
        log.info("验证码："+code);
        return Result.success(Message.SEND_SUCCESS);
    }
}
