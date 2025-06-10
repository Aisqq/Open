package com.me.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ElderDTO {
    private Integer elderId;
    @NotBlank(message = "姓名不能为空")
    private String name;
    private String gender;
    private Integer age;
    @NotBlank(message = "秘钥不能为空")
    private String secret_key;
}
