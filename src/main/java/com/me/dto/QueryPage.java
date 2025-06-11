package com.me.dto;

import lombok.Data;

@Data
public class QueryPage {
    private Integer start;
    private Integer size;
    private String query;
}
