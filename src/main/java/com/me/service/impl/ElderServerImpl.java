package com.me.service.impl;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.me.dao.ElderDao;
import com.me.dto.ElderDTO;
import com.me.dto.QueryPage;
import com.me.entity.Elder;
import com.me.service.ElderServer;
import com.me.utils.Message;
import com.me.utils.PageResult;
import com.me.utils.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class ElderServerImpl implements ElderServer {
    private final ElderDao elderDao;
    @Override
    public Result<String> addElder(ElderDTO elderDTO) {
        Elder elder = new Elder();
        elder.setAge(elderDTO.getAge());
        elder.setSecret_key(IdUtil.fastSimpleUUID());
        elder.setName(elderDTO.getName());
        elder.setGender(elderDTO.getGender());
        log.info(elder.toString());
        elderDao.addElder(elder);
        return Result.success(Message.SUCCESS);
    }

    @Override
    public PageResult findAllElder(QueryPage queryPage) {
        PageHelper.startPage(queryPage.getStart(),queryPage.getSize());
        Page<Elder> page = elderDao.findByCondition(queryPage.getQuery());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public Result<String> update(Elder elder) {
        if(elderDao.update(elder)>0){
            return Result.success(Message.UPDATE_SUCCESS);
        }
        return Result.error(Message.UPDATE_ERROR);
    }

}
