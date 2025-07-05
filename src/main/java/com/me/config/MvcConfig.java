package com.me.config;


import com.me.dao.UserDao;
import com.me.interceptor.AdminInterceptor;
import com.me.interceptor.LoginInterceptor;
import com.me.interceptor.RefreshTokenInterceptor;
import com.me.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;

@Configuration
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDao userDao;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RefreshTokenInterceptor(jwtTokenUtil,userDao)).addPathPatterns("/**").order(0);
        registry.addInterceptor(new LoginInterceptor()).excludePathPatterns("/api/user/pb/**","/api/captcha/**","/api/test/**","/api/amqp/**","/index.html").order(1);
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/api/admin/**")
                .order(2);
    }
    @Override
    public void addFormatters(@NotNull FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        registrar.registerFormatters(registry);
    }
}
