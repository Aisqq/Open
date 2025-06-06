package com.me.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (UserHolder.getUser() == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            Map<String, Object> result = new HashMap<>();
            result.put("code", 401);
            result.put("message", "无权限访问，请先登录");
            result.put("data", null);

            try (PrintWriter writer = response.getWriter()) {
                writer.write(new ObjectMapper().writeValueAsString(result));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
}
