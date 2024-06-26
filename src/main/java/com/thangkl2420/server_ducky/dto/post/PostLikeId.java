package com.thangkl2420.server_ducky.dto.post;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeId implements Serializable {
    @Column(name = "post_id")
    private Integer postId;
    @Column(name = "user_id")
    private Integer userId;
}