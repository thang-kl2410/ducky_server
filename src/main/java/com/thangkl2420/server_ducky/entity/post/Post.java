package com.thangkl2420.server_ducky.entity.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thangkl2420.server_ducky.entity.resource.ResourceFile;
import com.thangkl2420.server_ducky.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String content;
    private Long timestamp;
    @Column(nullable = false, columnDefinition = "int default 0")
    private int countComment;
    @Column(nullable = false, columnDefinition = "int default 0")
    private int countLike;

    @ElementCollection
    @CollectionTable(name = "post_images", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "images")
    private List<Integer> images;

    @JsonIgnore
    private Integer isComment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "parentPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> childrenPosts;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_post_id")
    private Post parentPost;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ResourceFile> resources;
}

