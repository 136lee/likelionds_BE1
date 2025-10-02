package com.example.likelionds_BE1.domain.post.post.service;

import com.example.likelionds_BE1.domain.member.member.entity.Member;
import com.example.likelionds_BE1.domain.post.post.domain.Post;
import com.example.likelionds_BE1.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.likelionds_BE1.domain.member.member.repository.MemberRepository;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public Post createPost(Post form) {
        Post post = new Post();
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        // user는 현재 로그인한 회원 정보로 세팅
        post.setUser(getCurrentUser());
        return postRepository.save(post);
    }

    private Member getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("로그인 사용자 정보 없음"));
    }



    public Post updatePost(Long id, Post post) {
        Post existing = getPost(id);
        existing.setTitle(post.getTitle());
        existing.setContent(post.getContent());
        existing.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(existing);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
