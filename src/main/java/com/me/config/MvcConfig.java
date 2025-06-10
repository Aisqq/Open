package com.me.config;


import com.me.dao.UserDao;
import com.me.interceptor.AdminInterceptor;
import com.me.interceptor.LoginInterceptor;
import com.me.interceptor.RefreshTokenInterceptor;
import com.me.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDao userDao;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RefreshTokenInterceptor(jwtTokenUtil,userDao)).addPathPatterns("/**").order(0);

        registry.addInterceptor(new LoginInterceptor()).excludePathPatterns("/api/user/pb/**","/api/captcha/**","/test/**").order(1);
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/api/admin/**")
                .order(2);
    }
}
