package com.me.service.impl;

import com.me.dao.UserDao;
import com.me.entity.User;
import com.me.service.UserService;
import com.me.utils.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl  implements UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDao userDao;
    private final JwtTokenUtil jwtTokenUtil;
    private final StringRedisTemplate stringRedisTemplate;
    @Override
    public Result<String> login(User userLogin, HttpServletResponse response) {
        User user;
        if(userLogin.getUsername().length()>9){
            user = userDao.findUserByPhone(userLogin.getUsername());
        }else {
            user = userDao.findUserByUsername(userLogin.getUsername());
        }
        if(!bCryptPasswordEncoder.matches(userLogin.getPassword(),user.getPassword())){
            return Result.error(Message.LOGIN_ERROR);
        }
        String jwtToken = jwtTokenUtil.generateToken(user);
        Cookie newCookie = CookieUtil.createJwtCookie(jwtToken);
        response.addCookie(newCookie);
        return Result.success(Message.LOGIN_SUCCESS);
    }

}
