package com.thangkl2420.server_ducky.controller;

import com.thangkl2420.server_ducky.entity.ResourceFile;
import com.thangkl2420.server_ducky.repository.RssRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/resource")
@RequiredArgsConstructor
public class ResourceController {
    private final RssRepository repository;

    private static final String uploadDir = "D:/ducky_resources/images";

    @PostMapping("/upload")
    public ResponseEntity<ResourceFile> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            byte[] fileBytes = file.getBytes();
            ResourceFile resourceFile = new ResourceFile();
            String fileName = UUID.randomUUID().toString() + "_" + sanitizeFileName(file.getOriginalFilename());
            resourceFile.setFileName(fileName);
            resourceFile.setFileType(file.getContentType());
            String filePath = uploadDir + fileName;
            resourceFile.setFilePath(filePath);
            repository.save(resourceFile);
            Files.write(Paths.get(filePath), fileBytes);
            //String fileUrl = "/api/v1/resources/" + resourceFile.getId();
            //String fileUrl = resourceFile.getId().toString();
            return ResponseEntity.ok(resourceFile);
        } catch (IOException e) {
            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
            return ResponseEntity.notFound().build();
        }
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
        Optional<ResourceFile> optionalFile = repository.findById(id);
        if (optionalFile.isPresent()) {
            ResourceFile file = optionalFile.get();
            byte[] imageData = null;
            if (imageData != null) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/video")
    public ResponseEntity<byte[]> getVideo(@PathVariable Integer id) {
        Optional<ResourceFile> optionalFile = repository.findById(id);
        if (optionalFile.isPresent()) {
            ResourceFile file = optionalFile.get();
            byte[] videoData = null;
            if (videoData != null) {
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(videoData);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable Integer id) {
        Optional<ResourceFile> optionalImage = repository.findById(id);

        if (optionalImage.isPresent()) {
            ResourceFile image = optionalImage.get();
            try {
                UrlResource resource = new UrlResource(Paths.get(image.getFilePath()).toUri());

                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping("/{filename}")
//    public ResponseEntity<Resource> getByFileName(@PathVariable Integer id) {
//        Optional<ResourceFile> optionalImage = repository.findById(id);
//
//        if (optionalImage.isPresent()) {
//            ResourceFile image = optionalImage.get();
//            try {
//                UrlResource resource = new UrlResource(Paths.get(image.getFilePath()).toUri());
//
//                return ResponseEntity.ok()
//                        .contentType(MediaType.IMAGE_JPEG)
//                        .body(resource);
//            } catch (IOException e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//            }
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
