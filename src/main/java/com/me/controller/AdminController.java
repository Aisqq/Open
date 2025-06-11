package com.me.controller;

import com.me.dto.ElderDTO;
import com.me.dto.QueryPage;
import com.me.service.AdminServer;
import com.me.utils.PageResult;
import com.me.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminServer adminServer;
    @PostMapping("/addElder")
    public Result<String> addElder(@Validated  @RequestBody ElderDTO elderDTO){
        return adminServer.addElder(elderDTO);
    }
    @GetMapping("/findAllElder")
    public PageResult findAllElder(@RequestBody QueryPage queryPage){
        return adminServer.findAllElder(queryPage);
    }
}
