package com.nhnacademy.security.controller;

import com.nhnacademy.security.domain.MemberDto;
import com.nhnacademy.security.domain.MemberId;
import com.nhnacademy.security.domain.MemberRegisterRequest;
import com.nhnacademy.security.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberRestController {

    private final MemberService memberService;

    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public MemberId createMember(@RequestBody MemberRegisterRequest request) {
        return memberService.createMember(request);
    }

    @GetMapping("/{memberId}")
    public MemberDto getMember(@PathVariable("memberId") String memberId) {
        return memberService.getMember(memberId);
    }

}
