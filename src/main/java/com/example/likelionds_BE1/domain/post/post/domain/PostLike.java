package com.example.likelionds_BE1.domain.post.post.domain;

import com.example.likelionds_BE1.domain.member.member.entity.Member;
import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "post_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "member_id"}))
public class PostLike {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected PostLike() {}
    public PostLike(Post post, Member member) { this.post = post; this.member = member; }

    public Long getId() { return id; }
    public Post getPost() { return post; }
    public Member getMember() { return member; }
}
