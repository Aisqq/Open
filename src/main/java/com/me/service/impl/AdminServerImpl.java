package com.me.service.impl;


import com.me.dto.ElderDTO;
import com.me.service.AdminServer;
import com.me.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class AdminServerImpl implements AdminServer {

    @Override
    public Result<String> addElder(ElderDTO elderDTO) {
        return null;
    }
}
