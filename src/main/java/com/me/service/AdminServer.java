package com.me.service;

import com.me.dto.ElderDTO;
import com.me.utils.Result;

public interface AdminServer {
    Result<String> addElder(ElderDTO elderDTO);
}
