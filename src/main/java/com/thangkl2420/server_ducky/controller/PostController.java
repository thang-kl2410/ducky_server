package com.thangkl2420.server_ducky.controller;

import com.thangkl2420.server_ducky.entity.post.Post;
import com.thangkl2420.server_ducky.service.PostService;
import lombok.RequiredArgsConstructor;
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
    ResponseEntity<Post> createPost(@RequestBody Post post, Principal connectUser){
        return ResponseEntity.ok(service.createPost(post, connectUser));
    }

    @PostMapping("/comment/{id}")
    ResponseEntity<Post> comment(@RequestBody Post post, @PathVariable(value = "id") Integer id, Principal connectUser){
        return ResponseEntity.ok(service.comment(post, id, connectUser));
    }

    @PostMapping("/update")
    ResponseEntity<Post> updatePost(@RequestBody Post post){
        return ResponseEntity.ok(service.updatePost(post));
    }

    @DeleteMapping("/delete")
    ResponseEntity<Boolean> deletePost(@Param(value = "id") Integer id){
        return ResponseEntity.ok(service.deletePost(id));
    }
}
