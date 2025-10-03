package com.example.likelionds_BE1.domain.post.post.repository;

import com.example.likelionds_BE1.domain.post.post.domain.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPost_IdOrderByIdAsc(Long postId);
    long countByPost_Id(Long postId);
    void deleteByPost_Id(Long postId);
}