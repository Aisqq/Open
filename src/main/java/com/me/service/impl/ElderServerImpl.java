package com.me.service.impl;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.me.dao.ElderDao;
import com.me.dto.ElderDTO;
import com.me.dto.QueryPage;
import com.me.entity.Elder;
import com.me.service.CacheService;
import com.me.service.ElderServer;
import com.me.utils.Message;
import com.me.utils.PageResult;
import com.me.utils.Result;
import com.me.vo.ElderVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class ElderServerImpl implements ElderServer {
    private final CacheService cacheService;
    private final ElderDao elderDao;
    @Override
    public Result<String> addElder(ElderDTO elderDTO) {
        Elder elder = new Elder();
        elder.setAge(elderDTO.getAge());
        elder.setSecretKey(IdUtil.fastSimpleUUID());
        elder.setName(elderDTO.getName());
        elder.setGender(elderDTO.getGender());
        log.info(elder.toString());
        elderDao.addElder(elder);
        return Result.success(Message.SUCCESS);
    }

    /**
     *  offset = (start-1)*size
     *  limit = size
     * @param queryPage
     *     start;第几页
     *     size;每页多少
     *     query;条件
     * @return
     */
    @Override
    public PageResult findAllElder(QueryPage queryPage) {
        if(queryPage.getStart()<10000){
            PageHelper.startPage(queryPage.getStart(), queryPage.getSize());
            Page<ElderVo> page = elderDao.findByCondition(queryPage.getQuery());
            return new PageResult(page.getTotal(), page.getResult());
        }
        Integer offset = (queryPage.getStart()-1) * queryPage.getSize();
        Integer limit = queryPage.getSize();
        String query = queryPage.getQuery();
        Long resultCount = cacheService.elderCount(query);
        return new PageResult(resultCount,elderDao.findByCondition2(offset,limit,query));
    }

    @Override
    public Result<String> update(Elder elder) {
        if(elderDao.update(elder)>0){
            return Result.success(Message.UPDATE_SUCCESS);
        }
        return Result.error(Message.UPDATE_ERROR);
    }

}
