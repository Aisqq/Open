package com.me.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ElderUpDTO {
    @NotNull(message = "id不能为空")
    private Integer elderId;
    @NotNull(message = "姓名不能为空")
    private String name;
    private String gender;
    private Integer age;
}