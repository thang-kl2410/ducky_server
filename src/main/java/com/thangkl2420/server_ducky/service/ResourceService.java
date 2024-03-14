package com.thangkl2420.server_ducky.service;

import com.thangkl2420.server_ducky.entity.ResourceFile;
import com.thangkl2420.server_ducky.repository.RssRepository;
import com.thangkl2420.server_ducky.dto.ResourceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final RssRepository repository;

    public void save(ResourceRequest request) {
        var book = ResourceFile.builder()
                .id(request.getId())
                .build();
        repository.save(book);
    }

    public List<ResourceFile> findAll() {
        return repository.findAll();
    }
}
