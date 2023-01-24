package com.nhnacademy.security.domain;

import lombok.Data;

@Data
public class MemberRegisterRequest {

    private String id;
    private String name;
    private String pwd;
    private String authority;


}
