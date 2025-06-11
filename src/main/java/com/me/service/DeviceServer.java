package com.me.service;

import com.me.entity.Device;
import com.me.utils.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DeviceServer {
    Result<String> uploadDevice(MultipartFile file);

    Result<List<Device>> findDevice(Integer id);
}
