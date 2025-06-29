package com.me.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Transactional
@RequestMapping("/api/test")
public class TestController {

}
