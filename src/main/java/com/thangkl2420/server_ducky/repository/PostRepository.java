package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.dto.post.PostDto;
import com.thangkl2420.server_ducky.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p WHERE p.isComment = 0 ORDER BY p.timestamp DESC")
    List<Post> findAllPost();
    @Query(value = "SELECT new com.thangkl2420.server_ducky.dto.post.PostDto(p, " +
            "CASE WHEN EXISTS (SELECT 1 FROM PostLike pl WHERE pl.id.postId = p.id AND pl.id.userId = :currentUserId) " +
            "THEN true ELSE false END) " +
            "FROM Post p " +
            "WHERE p.isComment = 0 " +
            "AND p.user.id = :id " +
            "ORDER BY p.timestamp DESC",
            countQuery = "select count(p)" +
                    "FROM Post p " +
                    "WHERE p.isComment = 0 " +
                    "AND p.user.id = :id " +
                    "ORDER BY p.timestamp DESC")
    Page<PostDto> findPostById(Integer id, Integer currentUserId,Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isComment = 1 AND p.parentPost.id = :id ORDER BY p.timestamp DESC")
    Page<Post> findCommentPost(Integer id, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isComment = 0 AND p.user.id = :id ORDER BY p.timestamp DESC")
    List<Post> findByUserId(Integer id);

    @Query("SELECT p " +
            "FROM Post p " +
            "WHERE p.isComment = 0 " +
            "AND (:startTime IS NULL OR p.timestamp >= :startTime) " +
            "AND (:endTime IS NULL OR p.timestamp <= :endTime) " +
            "AND (:keyWord IS NULL OR p.content LIKE CONCAT('%',:keyWord,'%')) " +
            "ORDER BY p.timestamp DESC"
            )
    Page<Post> filterPost(Long startTime, Long endTime, String keyWord, Pageable pageable);

    @Query("SELECT new com.thangkl2420.server_ducky.dto.post.PostDto(p, " +
            "CASE WHEN (SELECT COUNT(pl) FROM PostLike pl WHERE pl.id.postId = p.id AND pl.id.userId = :userId) > 0 " +
            "THEN true ELSE false END) " +
            "FROM Post p WHERE p IN :posts " +
            "ORDER BY p.timestamp DESC")
    List<PostDto> filterPostDto(@Param("posts") List<Post> posts, @Param("userId") Integer userId);

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
