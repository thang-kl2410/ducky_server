package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.dto.FollowingId;
import com.thangkl2420.server_ducky.entity.Following;
import com.thangkl2420.server_ducky.entity.Post;
import com.thangkl2420.server_ducky.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowingRepository extends JpaRepository<Following, FollowingId> {
    @Query("SELECT f.follower FROM Following f WHERE f.id.userId = :id")
    List<User> getAllFollower(Integer id);
    @Query("SELECT f.user FROM Following f WHERE f.id.followerId = :id")
    List<User> getAllWatcher(Integer id);

    @Query("SELECT p " +
            "FROM Following f JOIN Post p ON p.user.id = f.follower.id " +
            "WHERE f.id.userId = :id")
    List<Post> getAllFollowerPost(Integer id);
}
