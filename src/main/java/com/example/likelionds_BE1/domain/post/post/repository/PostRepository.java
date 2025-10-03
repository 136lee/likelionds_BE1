package com.example.likelionds_BE1.domain.post.post.repository;
import com.example.likelionds_BE1.domain.post.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByUser_IdOrderByCreatedAtDesc(Long userId);
    List<Post> findDistinctByTags_NameIgnoreCaseOrderByCreatedAtDesc(String name);
    long countByUser_Id(Long userId);
}
