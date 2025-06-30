package com.me.entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer userId;
    @NotNull(message = "用户名不能为空")
    private String username;
    private String password;
    @Length(max = 11,min = 11,message = "手机号必须11位")
    private String phone;
    private Integer elderId;
    private Integer role;
}