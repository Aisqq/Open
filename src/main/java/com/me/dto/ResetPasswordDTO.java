package com.me.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ResetPasswordDTO {

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    @Length(min = 6, max = 6, message = "验证码长度必须为6位")
    private String captcha;
}
