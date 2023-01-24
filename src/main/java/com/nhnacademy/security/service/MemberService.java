package com.nhnacademy.security.service;

import com.nhnacademy.security.domain.MemberDto;
import com.nhnacademy.security.domain.MemberId;
import com.nhnacademy.security.domain.MemberRegisterRequest;
import com.nhnacademy.security.entity.Authority;
import com.nhnacademy.security.entity.Member;
import com.nhnacademy.security.exception.MemberNotFoundException;
import com.nhnacademy.security.repository.MemberRepository;
import com.nhnacademy.security.util.PasswordUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Objects;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public MemberDto getMember(String id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));

        MemberDto memberDto = new MemberDto();
        memberDto.setId(member.getId());
        memberDto.setName(member.getName());
        memberDto.setPwd(member.getPwd());
//        memberDto.setSalt((member.getSalt()));
        memberDto.setAuthority(member.getAuthority().getAuthority());

        return memberDto;
    }

    @Transactional
    public MemberId createMember(MemberRegisterRequest request) {
//        SecureRandom random = new SecureRandom();
//        byte[] salt = new byte[8];
//        random.nextBytes(salt);

        Member member = new Member();
        member.setId(request.getId());
        member.setName(request.getName());
        //        member.setPwd(PasswordUtils.encode(request.getPwd(), salt));
        member.setPwd(passwordEncoder.encode(request.getPwd()));
        //        member.setSalt(PasswordUtils.bytesToHex(salt));


        Authority authority = new Authority();
        authority.setMember(member);
        authority.setAuthority(request.getAuthority());

        member.setAuthority(authority);

        memberRepository.saveAndFlush(member);

        MemberId memberId = new MemberId();
        memberId.setId(member.getId());

        return memberId;
    }

//    public boolean matches(String id, String pwd) {
//
//        Member member = memberRepository.findById(id)
//                .orElse(null);
//
//        if(Objects.isNull(member)) {
//            return  false;
//        }
//
////        String salt = member.getSalt();
//
////            return PasswordUtils.encode(pwd, salt.getBytes()).equals(member.getPwd());
//
//    }
}
