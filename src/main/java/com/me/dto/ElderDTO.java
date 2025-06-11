package com.me.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ElderDTO {
    @NotBlank(message = "姓名不能为空")
    private String name;
    private String gender;
    private Integer age;
}
