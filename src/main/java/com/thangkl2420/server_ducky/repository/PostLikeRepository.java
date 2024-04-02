package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.dto.PostLikeId;
import com.thangkl2420.server_ducky.entity.Post;
import com.thangkl2420.server_ducky.entity.PostLike;
import com.thangkl2420.server_ducky.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    @Query("SELECT pl.user FROM PostLike pl WHERE pl.post.isComment = 0 AND pl.id.postId = :id ORDER BY pl.post.timestamp DESC")
    List<User> findUserLikeByPostId(Integer id);
    @Query("SELECT pl.post FROM PostLike pl WHERE pl.id.userId = :id ORDER BY pl.post.timestamp DESC")
    List<Post> findLikePostByUserId(Integer id);
}
