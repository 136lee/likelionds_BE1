package com.example.likelionds_BE1.domain.post.post.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tag", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Tag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=50)
    private String name;

    protected Tag() {}
    public Tag(String name){ this.name = name; }

    public Long getId(){ return id; }
    public String getName(){ return name; }
}