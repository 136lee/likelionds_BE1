package com.example.likelionds_BE1.domain.member.member.repository;

import com.example.likelionds_BE1.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // username으로 회원 찾기 (로그인용)
    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);
}
