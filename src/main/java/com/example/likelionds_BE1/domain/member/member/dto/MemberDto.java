package com.example.likelionds_BE1.domain.member.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class MemberDto {
    private String username;
    private String password;
    private String nickname;
}
