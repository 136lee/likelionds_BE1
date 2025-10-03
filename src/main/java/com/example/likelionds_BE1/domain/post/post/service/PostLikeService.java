package com.example.likelionds_BE1.domain.post.post.service;

import com.example.likelionds_BE1.domain.member.member.entity.Member;
import com.example.likelionds_BE1.domain.member.member.repository.MemberRepository;
import com.example.likelionds_BE1.domain.post.post.domain.Post;
import com.example.likelionds_BE1.domain.post.post.domain.PostLike;
import com.example.likelionds_BE1.domain.post.post.repository.PostLikeRepository;
import com.example.likelionds_BE1.domain.post.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PostLikeService {

    private final PostLikeRepository likeRepo;
    private final PostRepository postRepo;
    private final MemberRepository memberRepo;

    public PostLikeService(PostLikeRepository likeRepo, PostRepository postRepo, MemberRepository memberRepo) {
        this.likeRepo = likeRepo;
        this.postRepo = postRepo;
        this.memberRepo = memberRepo;
    }

    public boolean toggle(Long postId, Long memberId) {
        if (likeRepo.existsByPost_IdAndMember_Id(postId, memberId)) {
            likeRepo.deleteByPost_IdAndMember_Id(postId, memberId);
            return false;
        } else {
            Post post = postRepo.getReferenceById(postId);
            Member member = memberRepo.getReferenceById(memberId);
            likeRepo.save(new PostLike(post, member));
            return true;
        }
    }

    public boolean liked(Long postId, Long memberId) {
        return likeRepo.existsByPost_IdAndMember_Id(postId, memberId);
    }

    public long count(Long postId) {
        return likeRepo.countByPost_Id(postId);
    }

    public List<PostLike> findMyLikes(Long memberId) {
        return likeRepo.findAllByMember_IdOrderByCreatedAtDesc(memberId);
    }

    public long countMyLikes(Long memberId) {
        return likeRepo.countByMember_Id(memberId);
    }

    public void deleteAllByPost(Long postId) {
        likeRepo.deleteByPost_Id(postId);
    }
}