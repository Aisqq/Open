package com.me.service;

import com.me.utils.Result;
import org.springframework.web.multipart.MultipartFile;

public interface DeviceServer {
    Result<String> uploadDevice(MultipartFile file);
}
