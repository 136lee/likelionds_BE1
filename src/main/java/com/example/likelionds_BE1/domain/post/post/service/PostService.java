package com.example.likelionds_BE1.domain.post.post.service;

import com.example.likelionds_BE1.domain.member.member.entity.Member;
import com.example.likelionds_BE1.domain.member.member.repository.MemberRepository;
import com.example.likelionds_BE1.domain.post.post.domain.Post;
import com.example.likelionds_BE1.domain.post.post.domain.Tag;
import com.example.likelionds_BE1.domain.post.post.repository.PostRepository;
import com.example.likelionds_BE1.domain.post.post.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeService likeService;
    private final TagRepository tagRepository;

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
        likeService.deleteAllByPost(id);
        postRepository.deleteById(id);
    }

    @Transactional
    public void addTag(Long postId, String tagName){
        var post = getPost(postId);
        var tag = tagRepository.findByNameIgnoreCase(tagName.trim())
                .orElseGet(() -> tagRepository.save(new Tag(tagName.trim())));
        post.getTags().add(tag);
    }

    @Transactional
    public void removeTag(Long postId, Long tagId){
        var post = getPost(postId);
        var tag = tagRepository.findById(tagId).orElseThrow();
        post.getTags().remove(tag);
    }

    public List<Post> findMyPosts(Long memberId) {
        return postRepository.findAllByUser_IdOrderByCreatedAtDesc(memberId);
    }

    public long countMyPosts(Long memberId) {
        return postRepository.countByUser_Id(memberId);
    }

    @Transactional
    public void replaceTags(Long postId, List<String> names) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post not found: " + postId));

        post.getTags().clear();

        if (names == null) return;

        names.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(String::toLowerCase)
                .distinct()
                .map(this::getOrCreateTag)
                .forEach(tag -> post.getTags().add(tag));
    }

    private Tag getOrCreateTag(String nameLower) {
        return tagRepository.findByNameIgnoreCase(nameLower)
                .orElseGet(() -> tagRepository.save(new Tag(nameLower)));
    }

    public List<Post> findByTagName(String name) {
        return postRepository.findDistinctByTags_NameIgnoreCaseOrderByCreatedAtDesc(name);
    }
}
