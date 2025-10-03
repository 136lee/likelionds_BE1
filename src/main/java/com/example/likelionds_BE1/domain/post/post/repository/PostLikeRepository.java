package com.example.likelionds_BE1.domain.post.post.repository;

import com.example.likelionds_BE1.domain.post.post.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPost_IdAndMember_Id(Long postId, Long memberId);
    long countByPost_Id(Long postId);
    void deleteByPost_IdAndMember_Id(Long postId, Long memberId);
    void deleteByPost_Id(Long postId);
    List<PostLike> findAllByMember_IdOrderByCreatedAtDesc(Long memberId);
    long countByMember_Id(Long memberId);
}