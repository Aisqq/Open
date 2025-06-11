package com.me.service.impl;


import com.me.dao.ElderDao;
import com.me.dto.ElderDTO;
import com.me.entity.Elder;
import com.me.service.AdminServer;
import com.me.utils.Message;
import com.me.utils.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class AdminServerImpl implements AdminServer {
    private final ElderDao elderDao;
    @Override
    public Result<String> addElder(ElderDTO elderDTO) {
        if(elderDao.findByKey(elderDTO.getSecret_key())!=null){
            return Result.error("秘钥已被使用");
        }

        Elder elder = new Elder();
        elder.setAge(elderDTO.getAge());
        elder.setSecret_key(elderDTO.getSecret_key());
        elder.setName(elderDTO.getName());
        elder.setGender(elderDTO.getGender());
        log.info(elder.toString());
        elderDao.addElder(elder);
        return Result.success(Message.SUCCESS);
    }
}
