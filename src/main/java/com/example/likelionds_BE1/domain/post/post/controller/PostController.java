package com.example.likelionds_BE1.domain.post.post.controller;

import com.example.likelionds_BE1.domain.post.post.domain.Post;
import com.example.likelionds_BE1.domain.post.post.service.PostService;
import com.example.likelionds_BE1.domain.member.member.entity.Member;
import com.example.likelionds_BE1.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberRepository memberRepository;

    // 모든 게시물 목록
    @GetMapping
    public String list(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "post/list"; // src/main/resources/templates/post/list.html
    }

    // 글 작성 페이지
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("post", new Post());
        return "post/create"; // src/main/resources/templates/post/create.html
    }

    // 글 작성 처리
    @PostMapping
    public String create(Post post) {
        post.setUser(getCurrentUser());
        postService.createPost(post);
        return "redirect:/posts";
    }

    // 게시물 상세 페이지
    @GetMapping("/{id:\\d+}")
    public String detail(@PathVariable Long id, Model model) {
        Post post = postService.getPost(id);
        model.addAttribute("post", post);
        return "post/detail"; // src/main/resources/templates/post/detail.html
    }

    // 글 수정 페이지
    @GetMapping("/{id:\\d+}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Post post = postService.getPost(id);
        model.addAttribute("post", post);
        return "post/edit"; // src/main/resources/templates/post/edit.html
    }

    // 글 수정 처리
    @PostMapping("/{id:\\d+}/edit")
    public String edit(@PathVariable Long id, Post post) {
        postService.updatePost(id, post);
        return "redirect:/posts/" + id;
    }

    // 글 삭제
    @PostMapping("/{id:\\d+}/delete")
    public String delete(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    // 로그인한 사용자 가져오기
    private Member getCurrentUser() {
        String username = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("로그인 사용자 정보 없음"));
    }
}
