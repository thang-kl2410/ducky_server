package com.thangkl2420.server_ducky.controller;

import com.thangkl2420.server_ducky.entity.Post;
import com.thangkl2420.server_ducky.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService service;

    @GetMapping("/get-all")
    ResponseEntity<List<Post>> getAllPost(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/get-comment/{id}")
    ResponseEntity<List<Post>> getComments(@PathVariable(value = "id") Integer id){
        return ResponseEntity.ok(service.getComments(id));
    }

    @GetMapping("/like/{id}")
    ResponseEntity<List<Post>> handleLike(@PathVariable(value = "id") Integer id, Principal connectUser){
        service.handleLike(id, connectUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    ResponseEntity<?> createPost(@RequestBody Post post, Principal connectUser){
        service.createPost(post, connectUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comment/{id}")
    ResponseEntity<?> comment(@RequestBody Post post, @PathVariable(value = "id") Integer id, Principal connectUser){
        service.comment(post, id, connectUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    ResponseEntity<?> updatePost(@RequestBody Post post){
        service.updatePost(post);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    ResponseEntity<?> deletePost(@Param(value = "id") Integer id){
        service.deletePost(id);
        return ResponseEntity.ok().build();
    }
}
