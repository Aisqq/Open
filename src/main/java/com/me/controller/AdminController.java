package com.me.controller;

import com.me.dto.ElderDTO;
import com.me.dto.ElderUpDTO;
import com.me.dto.QueryPage;
import com.me.entity.Device;
import com.me.entity.Elder;
import com.me.service.ElderServer;
import com.me.service.DeviceServer;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final DeviceServer deviceServer;
    private final ElderServer elderServer;
    @PostMapping("/addElder")
    public Result<String> addElder(@Validated @RequestBody ElderDTO elderDTO){
        return elderServer.addElder(elderDTO);
    }
    @PostMapping("/findAllElder")
    public PageResult findAllElder(@RequestBody QueryPage queryPage){
        return elderServer.findAllElder(queryPage);
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
    @PostMapping("/upload")
    public Result<String> uploadDevice(@RequestParam ("file") MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.endsWith(".xlsx")) {
            return Result.error("文件格式错误，仅支持.xlsx格式");
        }
        return deviceServer.uploadDevice(file);
    }
    @GetMapping("/findDevice/{id}")
    public Result<List<Device>> findDevice(@PathVariable Integer id){
        return deviceServer.findDevice(id);
    }

    @PutMapping("/updateElder")
    public Result<String> updateElder(@Validated @RequestBody ElderUpDTO elderDTO){
        Elder elder = new Elder();
        elder.setElderId(elderDTO.getElderId());
        elder.setName(elderDTO.getName());
        elder.setGender(elderDTO.getGender());
        elder.setAge(elderDTO.getAge());
        return elderServer.update(elder);
    }
}
