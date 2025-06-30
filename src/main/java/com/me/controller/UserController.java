package com.me.controller;

import com.me.service.AlarmLogService;
import com.me.utils.PageResult;
import com.me.vo.ElderVo;
import com.me.vo.OutStatusVO;
import com.me.dto.RegisterDTO;
import com.me.dto.ResetPasswordDTO;
import com.me.entity.User;
import com.me.service.UserService;
import com.me.utils.Message;
import com.me.utils.RedisKey;
import com.me.utils.Result;
import com.me.vo.UserVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    public Result<BigDecimal> getWaterUsage(
            @RequestParam(value = "date", required = false)  LocalDateTime date) {
        if (date == null) {
            date = LocalDateTime.now();
        }
        date = date.toLocalDate().atStartOfDay();
        return userService.getWaterUsage(date);
    }
    @GetMapping("/getTemp")
    public Result<BigDecimal> getTemp(@RequestParam(value = "date", required = false) LocalDateTime date) {
        if (date == null) {
            date = LocalDateTime.now();
        }
        date = date.toLocalDate().atStartOfDay();
        return userService.getTemp(date);
    }
    @GetMapping("/getOutStatus")
    public Result<OutStatusVO> getMovementStats( @RequestParam(value = "date", required = false) LocalDateTime date) {
        if (date == null) {
            date = LocalDateTime.now();
        }
        date = date.toLocalDate().atStartOfDay();
        return userService.getMovementStats(date);
    }
    @GetMapping("/getTurnOverCount")
    public Result<Integer> getTurnOverCount( @RequestParam(value = "date", required = false)LocalDateTime date) {
        if (date == null) {
            date = LocalDateTime.now();
        }
        date = date.toLocalDate().atStartOfDay();
        return userService.getTurnOverCount(date);
    }

    @GetMapping("/getSmog")
    public Result<BigDecimal> getSmog( @RequestParam(value = "date", required = false)  LocalDateTime date) {
        if (date == null) {
            date = LocalDateTime.now();
        }
        date = date.toLocalDate().atStartOfDay();
        return userService.getSmog(date);
    }

    @PutMapping("/alarm-logs/{alarmId}/status")
    public Result<String> alarmStatus(@PathVariable("alarmId") Integer alarmId){
        return alarmLogService.alarmStatus(alarmId);
    }

    @GetMapping("/alarm-logs/recent-30-days")
    public PageResult getRecent30DaysAlarmLogs(@PathParam("page")Integer page, @PathParam("pageSize") Integer pageSize, @RequestParam(value = "type", required = false) String type) {
        int start = page != null ? page : 0;
        int size = pageSize != null ? pageSize : 10;
        if (start < 0) {
            start = 0;
        }
        if (size <= 0 || size > 100) {
            size = 10;
        }
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minus(30, ChronoUnit.DAYS);
        return alarmLogService.getAlarmLogsByTimeRange(startTime, endTime,start,size,type);
    }
    @GetMapping("/getElder")
    public Result<ElderVo> getElder(){
        return userService.getElder();
    }
    @GetMapping("/getUser")
    public Result<UserVo> getUser(){
        return userService.getUser();
    }
    @PutMapping("/editUser")
    public Result<String> editUser(@Valid @RequestBody User user,HttpServletResponse response){
        return userService.edit(user,response);
    }
}
