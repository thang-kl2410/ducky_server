package com.thangkl2420.server_ducky.controller;

import com.thangkl2420.server_ducky.dto.FilterRequest;
import com.thangkl2420.server_ducky.dto.post.PostDto;
import com.thangkl2420.server_ducky.entity.post.Post;
import com.thangkl2420.server_ducky.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @PostMapping("/get-all")
    ResponseEntity<List<PostDto>> getAllPost(@RequestBody FilterRequest request, Principal connectedUser){
        return ResponseEntity.ok(service.getAll(request, connectedUser));
    }


    @GetMapping("/post/user/{id}")
    ResponseEntity<List<PostDto>> getPostByUser(@PathVariable(value = "id") Integer id, @Param(value = "pageIndex") Integer pageIndex,
                                                @Param(value = "pageSize") Integer pageSize, Principal connectedUser){
        return ResponseEntity.ok(service.getPostByUser(id, pageIndex, pageSize, connectedUser));
    }

    @GetMapping("/get-comment/{id}")
    ResponseEntity<List<Post>> getComments(@PathVariable(value = "id") Integer id, @Param(value = "pageIndex") Integer pageIndex, @Param(value = "pageSize") Integer pageSize){
        return ResponseEntity.ok(service.getComments(id, pageIndex, pageSize));
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

//    @GetMapping("/filter")
//    ResponseEntity<List<Post>> filterPost(@Param(value = "startTime") long startTime, @Param(value = "endTime") long endTime, @Param(value = "keyword") String keyword){
//        return ResponseEntity.ok(service.filterPost(startTime, endTime, keyword));
//    }

    @GetMapping("/follower")
    ResponseEntity<List<PostDto>> followerPost(Principal connectedUser, @Param(value = "pageIndex") Integer pageIndex, @Param(value = "pageSize") Integer pageSize){
        return ResponseEntity.ok(service.getFollowersPost(connectedUser, pageIndex, pageSize));
    }

    @GetMapping("/user/{id}")
    ResponseEntity<List<PostDto>> userPost(@PathVariable(value = "id") Integer id, @Param(value = "pageIndex") Integer pageIndex,
                                           @Param(value = "pageSize") Integer pageSize, Principal connectedUser){
        return ResponseEntity.ok(service.getAllPostByIdUser(id, pageIndex, pageSize, connectedUser));
    }
}
