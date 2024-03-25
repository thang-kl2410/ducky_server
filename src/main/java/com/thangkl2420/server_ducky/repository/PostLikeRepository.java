package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.dto.PostLikeId;
import com.thangkl2420.server_ducky.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
}
