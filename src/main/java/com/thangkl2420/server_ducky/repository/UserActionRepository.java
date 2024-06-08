package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.entity.user.UserAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActionRepository extends JpaRepository<UserAction, Integer> {
}
