package com.me.controller;

import com.me.entity.AlarmLog;
import com.me.service.AlarmLogService;
import com.me.vo.OutStatusVO;
import com.me.dto.RegisterDTO;
import com.me.dto.ResetPasswordDTO;
import com.me.entity.User;
import com.me.service.UserService;
import com.me.utils.Message;
import com.me.utils.RedisKey;
import com.me.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final AlarmLogService alarmLogService;
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
    @PostMapping("/pb/register")
    public Result<String> register(@Valid @RequestBody RegisterDTO dto){
        return userService.register(dto);
    }

    @PostMapping("/setPassword")
    public Result<String> setPassword(@RequestBody Map<String,String> map){
        return userService.setPassword(map.get("password"));
    }
    @PostMapping("/pb/verifyCode")
    public Result<String> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO,HttpServletResponse response){
        return userService.resetPassword(resetPasswordDTO.getPhone(),resetPasswordDTO.getCode(),response);
    }


    @GetMapping("/getWater")
    public Result<BigDecimal> getWaterUsage() {
        return userService.getWaterUsage();
    }
    @GetMapping("/getTemp")
    public Result<BigDecimal> getTemp() {
        return userService.getTemp();
    }
    @GetMapping("/getOutStatus")
    public Result<OutStatusVO> getMovementStats() {
        return userService.getMovementStats();
    }
    @GetMapping("/getTurnOverCount")
    public Result<Integer> getTurnOverCount() {
        return userService.getTurnOverCount();
    }
    @GetMapping("/alarm-logs/recent-30-days")
    public Result<List<AlarmLog>> getRecent30DaysAlarmLogs() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minus(30, ChronoUnit.DAYS);
        return alarmLogService.getAlarmLogsByTimeRange(startTime, endTime);
    }
}
