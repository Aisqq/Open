package com.me.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private DataSource dataSource;

    @GetMapping("/connection")
    public String testConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return "成功连接到OpenGauss数据库! 连接信息: " +
                    "\nURL: " + connection.getMetaData().getURL() +
                    "\nDatabase: " + connection.getMetaData().getDatabaseProductName() +
                    "\nVersion: " + connection.getMetaData().getDatabaseProductVersion();
        } catch (SQLException e) {
            return "连接OpenGauss失败! 错误信息: " + e.getMessage();
        }
    }
}