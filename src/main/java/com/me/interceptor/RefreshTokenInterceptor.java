package com.me.interceptor;

import com.me.dao.UserDao;
import com.me.entity.User;
import com.me.utils.CookieUtil;
import com.me.utils.JwtTokenUtil;
import com.me.utils.UserHolder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Date;
import java.util.Objects;

@Slf4j
public class RefreshTokenInterceptor implements HandlerInterceptor {
    private final String COOKIE_NAME = "jwtToken";
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDao userDao;
    public RefreshTokenInterceptor(JwtTokenUtil jwtTokenUtil,UserDao userDao) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDao = userDao;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String origin = request.getHeader("Origin");
        log.info("origin:"+origin);
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        log.info(Objects.requireNonNullElse(token, "null"));
        try {
            if(token==null||token.isEmpty()||isTokenExpired(token)){
                return true;
            }
        }catch (Exception e){
            clearTokenCookies(response);
            return true;
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = null;
        if(username!=null){
            user = userDao.findUserByUsername(username);
        }
        if(user!=null&&jwtTokenUtil.validateToken(token,user)){
            UserHolder.saveUser(user);
            Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);
            long remainingTime = expiration.getTime() - System.currentTimeMillis();
            long refreshThreshold = 15 * 60 * 1000;

            if (remainingTime < refreshThreshold) {
                String newToken = jwtTokenUtil.generateToken(user);
                Cookie newCookie = CookieUtil.createJwtCookie(newToken);
                response.addCookie(newCookie);

            }
        }
        return true;
    }
    private boolean isTokenExpired(String token) {
        Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    private void clearTokenCookies(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(UserHolder.getUser()!=null){
            UserHolder.removeUser();
        }
    }
}
