package com.example.assigment_java6.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {
    private int page;
    private int pageSize;
    private int pages;
    private int total;
}
