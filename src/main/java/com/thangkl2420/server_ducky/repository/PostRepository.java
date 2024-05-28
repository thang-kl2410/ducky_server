package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p WHERE p.isComment = 0 ORDER BY p.timestamp DESC")
    List<Post> findAllPost();
    @Query("SELECT p FROM Post p WHERE p.isComment = 0 AND p.user.id = :id ORDER BY p.timestamp DESC")
    List<Post> findPostById(Integer id);
    @Query("SELECT p FROM Post p WHERE p.isComment = 1 AND p.parentPost.id = :id ORDER BY p.timestamp DESC")
    List<Post> findCommentPost(Integer id);

    @Query("SELECT p FROM Post p WHERE p.isComment = 0 AND p.user.id = :id ORDER BY p.timestamp DESC")
    List<Post> findByUserId(Integer id);

    @Query("SELECT p FROM Post p WHERE p.isComment = 0 " +
            "AND (:startTime IS NULL OR p.timestamp >= :startTime) " +
            "AND (:endTime = 0 OR :endTime IS NULL OR p.timestamp <= :endTime) " +
            "AND p.content LIKE %:keyWord% " +
            "ORDER BY p.timestamp DESC")
    List<Post> filterPost(long startTime, long endTime, String keyWord);
}
