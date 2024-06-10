package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p WHERE p.isComment = 0 ORDER BY p.timestamp DESC")
    List<Post> findAllPost();
    @Query("SELECT p FROM Post p WHERE p.isComment = 0 AND p.user.id = :id ORDER BY p.timestamp DESC")
    Page<Post> findPostById(Integer id,Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.isComment = 1 AND p.parentPost.id = :id ORDER BY p.timestamp DESC")
    Page<Post> findCommentPost(Integer id, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isComment = 0 AND p.user.id = :id ORDER BY p.timestamp DESC")
    List<Post> findByUserId(Integer id);

    @Query("SELECT p FROM Post p WHERE p.isComment = 0 " +
            "AND (:startTime IS NULL OR p.timestamp >= :startTime) " +
            "AND (:endTime IS NULL OR p.timestamp <= :endTime) " +
            "AND p.content LIKE %:keyWord% " +
            "ORDER BY p.timestamp DESC")
    Page<Post> filterPost(long startTime, long endTime, String keyWord, Pageable pageable);

    @Modifying
    @Query("DELETE FROM PostLike pl where pl.id.postId = :id")
    void deleteLikePostById(Integer id);

    @Modifying
    @Query("DELETE FROM Post p where p.parentPost.id = :id")
    void deleteCommentPostById(Integer id);

    @Modifying
    @Query("DELETE FROM Post p where p.id = :id")
    void deletePostById(Integer id);
}
