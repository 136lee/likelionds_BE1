package com.example.likelionds_BE1.domain.post.post.repository;


import com.example.likelionds_BE1.domain.post.post.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByNameIgnoreCase(String name);
}