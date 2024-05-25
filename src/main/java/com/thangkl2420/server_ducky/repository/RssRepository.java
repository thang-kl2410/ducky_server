package com.thangkl2420.server_ducky.repository;

import com.thangkl2420.server_ducky.entity.resource.ResourceFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RssRepository extends JpaRepository<ResourceFile, Integer> {
    Optional<ResourceFile> findByFileName(String name);
}
