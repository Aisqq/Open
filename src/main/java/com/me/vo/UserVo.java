package com.me.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer userId;
    private String username;
    private String phone;
}
