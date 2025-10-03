// src/main/java/com/example/likelionds_BE1/domain/member/member/controller/MemberPageController.java
package com.example.likelionds_BE1.domain.member.member.controller;

import com.example.likelionds_BE1.domain.member.member.repository.MemberRepository;
import com.example.likelionds_BE1.domain.post.post.service.PostLikeService;
import com.example.likelionds_BE1.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberPageController {

    private final MemberRepository memberRepository;
    private final PostService postService;
    private final PostLikeService likeService;

    @GetMapping("/mypage")
    public String mypage() {
        return "member/mypage";
    }

    @GetMapping("/myposts")
    public String myPosts(Model model) {
        Long meId = currentUserId();
        model.addAttribute("posts", postService.findMyPosts(meId));
        model.addAttribute("postCount", postService.countMyPosts(meId));
        return "member/my-posts";
    }

    @GetMapping("/likes")
    public String myLikes(Model model) {
        Long meId = currentUserId();
        model.addAttribute("likes", likeService.findMyLikes(meId));
        model.addAttribute("likeCount", likeService.countMyLikes(meId));
        return "member/my-likes";
    }

    private Long currentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("로그인 사용자 정보 없음"))
                .getId();
    }
}