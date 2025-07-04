package com.me.controller;


import com.me.annotation.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;



@RequiredArgsConstructor
@RestController
@Transactional
@RequestMapping("/api/test")
public class TestController {


    @GetMapping("/t1")
    public String hello(){
        return "hello";
    }

    @Role("admin")
    @GetMapping("/t2")
    public String hello2(){
        return "hello1";
    }
}
