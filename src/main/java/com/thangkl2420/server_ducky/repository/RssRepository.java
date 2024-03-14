package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.entity.ResourceFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RssRepository extends JpaRepository<ResourceFile, Integer> {
}
