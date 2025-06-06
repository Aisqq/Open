package com.me.controller;

import com.me.entity.User;
import com.me.service.UserService;
import com.me.utils.Message;
import com.me.utils.RedisKey;
import com.me.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;
    private final StringRedisTemplate stringRedisTemplate;
    /**
     * 登录
     * @param map 接收数据
     * @return
     */
    @PostMapping("/pb/login")
    public Result<String> login(@RequestBody Map<String,String> map, HttpServletRequest request, HttpServletResponse response){
        String captcha = map.get("captcha");
        if (captcha == null) {
            return Result.error(Message.VALIDATION_ERROR);
        }

        String storedCaptcha = stringRedisTemplate.opsForValue().get(RedisKey.CAPTCHA + request.getSession().getId());
        if (storedCaptcha == null || !storedCaptcha.equalsIgnoreCase(captcha)) {
            return Result.error(Message.VALIDATION_ERROR);
        }
        if(map.get("username")==null||map.get("password")==null){
            return Result.error(Message.INVALID);
        }
        User userLogin = new User();
        userLogin.setUsername(map.get("username"));
        userLogin.setPassword(map.get("password"));
        return  userService.login(userLogin,response);
    }


}
