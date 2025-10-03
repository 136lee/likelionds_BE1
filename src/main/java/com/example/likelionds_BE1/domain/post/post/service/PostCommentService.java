package com.example.likelionds_BE1.domain.post.post.service;

import com.example.likelionds_BE1.domain.member.member.entity.Member;
import com.example.likelionds_BE1.domain.member.member.repository.MemberRepository;
import com.example.likelionds_BE1.domain.post.post.domain.Post;
import com.example.likelionds_BE1.domain.post.post.domain.PostComment;
import com.example.likelionds_BE1.domain.post.post.repository.PostCommentRepository;
import com.example.likelionds_BE1.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostCommentService {

    private final PostCommentRepository commentRepo;
    private final PostRepository postRepo;
    private final MemberRepository memberRepo;

    @Transactional(readOnly = true)
    public List<PostComment> getForPost(Long postId) {
        return commentRepo.findByPost_IdOrderByIdAsc(postId);
    }

    public void write(Long postId, Long memberId, String content) {
        Post post = postRepo.getReferenceById(postId);
        Member user = memberRepo.getReferenceById(memberId);

        PostComment c = PostComment.builder()
                .post(post)
                .user(user)
                .content(content)
                .build();
        commentRepo.save(c);
    }

    @Transactional(readOnly = true)
    public PostComment get(Long commentId) {
        return commentRepo.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
    }

    public Long edit(Long commentId, Long actorId, String content) {
        PostComment c = get(commentId);
        if (!c.getUser().getId().equals(actorId)) {
            throw new AccessDeniedException("본인 댓글만 수정할 수 있습니다.");
        }
        c.setContent(content);
        return c.getPost().getId();
    }

    public Long delete(Long commentId, Long actorId) {
        PostComment c = get(commentId);
        if (!c.getUser().getId().equals(actorId)) {
            throw new AccessDeniedException("본인 댓글만 삭제할 수 있습니다.");
        }
        Long postId = c.getPost().getId();
        commentRepo.delete(c);
        return postId;
    }
}