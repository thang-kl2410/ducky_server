package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.dto.user.FollowingId;
import com.thangkl2420.server_ducky.entity.user.Following;
import com.thangkl2420.server_ducky.entity.post.Post;
import com.thangkl2420.server_ducky.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowingRepository extends JpaRepository<Following, FollowingId> {
    @Query("SELECT f.follower FROM Following f WHERE f.id.userId = :id")
    List<User> getAllFollower(Integer id);
    @Query("SELECT f.user FROM Following f WHERE f.id.followerId = :id")
    List<User> getAllWatcher(Integer id);

    @Query("SELECT p " +
            "FROM Following f JOIN Post p ON p.user.id = f.id.followerId " +
            "WHERE f.id.userId = :id AND p.isComment = 0 ORDER BY p.timestamp DESC")
    Page<Post> getAllFollowerPost(Integer id, Pageable pageable);
}
