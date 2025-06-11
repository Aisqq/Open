package com.me.service;

import com.me.dto.ElderDTO;
import com.me.dto.QueryPage;
import com.me.utils.PageResult;
import com.me.utils.Result;

public interface AdminServer {
    Result<String> addElder(ElderDTO elderDTO);


    PageResult findAllElder(QueryPage queryPage);
}
