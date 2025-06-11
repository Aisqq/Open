package com.me.controller;

import com.me.dto.ElderDTO;
import com.me.dto.QueryPage;
import com.me.service.AdminServer;
import com.me.utils.PageResult;
import com.me.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminServer adminServer;
    @PostMapping("/addElder")
    public Result<String> addElder(@Validated @RequestBody ElderDTO elderDTO){
        return adminServer.addElder(elderDTO);
    }
    @GetMapping("/findAllElder")
    public PageResult findAllElder(@RequestBody QueryPage queryPage){
        return adminServer.findAllElder(queryPage);
    }
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadTemplate() {
        Resource resource = new ClassPathResource("template/template.xlsx");
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=template.xlsx");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
