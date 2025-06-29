package com.me.service.impl;

import com.me.dao.ElderDao;
import com.me.dao.UserDao;
import com.me.vo.ElderVo;
import com.me.vo.OutStatusVO;
import com.me.dto.RegisterDTO;
import com.me.entity.Elder;
import com.me.entity.User;
import com.me.service.UserService;
import com.me.utils.*;
import com.me.vo.UserVo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl  implements UserService {
    private final ElderDao elderDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final StringRedisTemplate stringRedisTemplate;
    private final UserDao userDao;
    private final JwtTokenUtil jwtTokenUtil;
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

    @Override
    public Result<String> register(RegisterDTO dto) {
        Elder elder = elderDao.findByKey(dto.getKey());
        if(elder==null)
            return Result.error(Message.Elder_NOT_EXIST);
        if(userDao.findUserByUsername(dto.getUsername())!=null)
            return Result.error(Message.USER_EXIST);
        if(userDao.findUserByPhone(dto.getPhone())!=null)
            return Result.error(Message.PHONE_EXIST);
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setElderId(elder.getElderId());
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        userDao.save(user);
        return Result.success(Message.SUCCESS);
    }

    @Override
    public Result<String> setPassword(String password) {
        User user = UserHolder.getUser();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userDao.update(user);
        return Result.success(Message.SUCCESS);
    }

    @Override
    public Result<String> resetPassword(String phone, String code, HttpServletResponse response) {
        if(!Objects.equals(stringRedisTemplate.opsForValue().get(RedisKey.FORGET_CAPTCHA + phone), code)){
            return Result.error(Message.VALIDATION_ERROR);
        }
        User user = userDao.findUserByPhone(phone);
        String jwtToken  = jwtTokenUtil.generateToken(user);
        Cookie cookie = CookieUtil.createJwtCookie(jwtToken);
        response.addCookie(cookie);
        return Result.success(Message.SUCCESS);
    }


    @Override
    public Result<BigDecimal> getWaterUsage(LocalDateTime date) {
        User user = UserHolder.getUser();
        Integer elderId = user.getElderId();
        BigDecimal waterUsage = elderDao.findWaterUsage(elderId, date);
        log.info("用水量："+waterUsage);
        return Result.success(Message.SUCCESS, waterUsage);
    }
    @Override
    public Result<BigDecimal> getTemp(LocalDateTime date) {
        User user = UserHolder.getUser();
        Integer elderId = user.getElderId();
        BigDecimal temp = elderDao.getLatestTemperatureValue(elderId, date);
        log.info("体温："+temp);
        return Result.success(Message.SUCCESS, temp);
    }
    @Override
    public Result<OutStatusVO> getMovementStats(LocalDateTime date) {
        User user = UserHolder.getUser();
        Integer elderId = user.getElderId();
        Integer outTimes = elderDao.countOutTimes(elderId, date);
        Integer homeTimes = elderDao.countHomeTimes(elderId, date);
        OutStatusVO status = new OutStatusVO(outTimes, homeTimes);
        log.info("外出情况："+status);
        return Result.success(Message.SUCCESS, status);
    }

    @Override
    public Result<Integer> getTurnOverCount(LocalDateTime date) {
        User user = UserHolder.getUser();
        Integer elderId = user.getElderId();
        return Result.success(Message.SUCCESS,elderDao.turnOverCount(elderId,date));
    }

    @Override
    public Result<BigDecimal> getSmog(LocalDateTime date) {
        User user = UserHolder.getUser();
        Integer elderId = user.getElderId();
        return Result.success(Message.SUCCESS,elderDao.getAverageSmogLevel(elderId,date));
    }

    @Override
    public Result<ElderVo> getElder() {
        User user = UserHolder.getUser();
        Integer elderId = user.getElderId();
        Elder elder = elderDao.findById(elderId);
        ElderVo elderVo = new ElderVo();
        elderVo.setElderId(elderId);
        elderVo.setAge(elder.getAge());
        elderVo.setGender(elder.getGender());
        elderVo.setName(elder.getName());
        return Result.success(Message.SUCCESS,elderVo);
    }

    @Override
    public Result<UserVo> getUser() {
        User user = UserHolder.getUser();
        UserVo userVo = new UserVo();
        userVo.setUserId(user.getUserId());
        userVo.setUsername(user.getUsername());
        userVo.setPhone(user.getPhone());
        return Result.success(Message.SUCCESS,userVo);
    }

    @Override
    public Result<String> edit(User user) {
        user.setUserId(UserHolder.getUser().getUserId());
        if(user.getPhone()==null||userDao.findUserByUsername(user.getUsername())!=null||userDao.findUserByPhone(user.getPhone())!=null){
            return Result.error(Message.ERROR);
        }
        userDao.update(user);
        return Result.success(Message.SUCCESS);
    }
}
