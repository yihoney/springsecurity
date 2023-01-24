package com.nhnacademy.security.domain;

import lombok.Data;

@Data
public class MemberDto {
    private String id;
    private String name;
    private String pwd;
//    private String salt;
    private String authority;
}
