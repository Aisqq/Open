package com.me.controller;

import com.me.dto.ElderDTO;
import com.me.service.AdminServer;
import com.me.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminServer adminServer;
    @PostMapping("/addElder")
    public Result<String> addElder(@Validated  @RequestBody ElderDTO elderDTO){
        return adminServer.addElder(elderDTO);
    }
}
