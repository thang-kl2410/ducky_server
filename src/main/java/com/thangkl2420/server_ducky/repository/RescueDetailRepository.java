package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.dto.RescueDetailId;
import com.thangkl2420.server_ducky.entity.RescueDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RescueDetailRepository extends JpaRepository<RescueDetail, RescueDetailId> {
}
