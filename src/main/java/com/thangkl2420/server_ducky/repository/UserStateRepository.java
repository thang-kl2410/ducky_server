package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.entity.UserState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStateRepository extends JpaRepository<UserState, Integer> {
    Optional<UserState> findById(Integer id);
}
