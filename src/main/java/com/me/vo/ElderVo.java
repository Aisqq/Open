package com.me.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElderVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer elderId;
    private String name;
    private String gender;
    private Integer age;
}