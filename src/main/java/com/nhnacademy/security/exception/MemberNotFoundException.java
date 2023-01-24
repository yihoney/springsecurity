package com.nhnacademy.security.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String id) {
        super("member not found: member_id= " + id);
    }
}
