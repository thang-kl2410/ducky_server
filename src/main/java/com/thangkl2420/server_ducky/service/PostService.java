package com.thangkl2420.server_ducky.service;

import com.thangkl2420.server_ducky.dto.PostLikeId;
import com.thangkl2420.server_ducky.entity.Post;
import com.thangkl2420.server_ducky.entity.PostLike;
import com.thangkl2420.server_ducky.entity.User;
import com.thangkl2420.server_ducky.repository.PostLikeRepository;
import com.thangkl2420.server_ducky.repository.PostRepository;
import com.thangkl2420.server_ducky.repository.RescueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    final private PostRepository repository;
    final private PostLikeRepository postLikeRepository;

    public List<Post> getAll(){
        return repository.findAllPost();
    }

    public List<Post> getAllMyPost(Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return repository.findPostById(user.getId());
    }

    public List<Post> getComments(Integer idPost){
        return repository.findCommentPost(idPost);
    }

    public void handleLike(Integer idPost, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Post post = repository.findById(idPost).orElse(null);

        if(post != null){
            PostLikeId id = new PostLikeId(idPost, user.getId());
            PostLike postLike = new PostLike(id, post, user);
            postLikeRepository.save(postLike);
        }
    }

    public void createPost(Post post, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        post.setUser(user);
        post.setIsComment(0);
        repository.save(post);
    }

    public void comment(Post post, Integer parentPostId, Principal connectedUser){
        Post parent = repository.findById(parentPostId).orElse(null);
        if(parent != null){
            var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            post.setUser(user);
            post.setIsComment(1);
            post.setParentPost(parent);
            repository.save(post);
        }
    }

    public void updatePost(Post post){
        Post p = repository.findById(post.getId()).orElseThrow(null);
        if(p != null){
            p.setUser(post.getUser());
            p.setId(post.getId());
            p.setContent(post.getContent());
            p.setIsComment(post.getIsComment());
            p.setParentPost(post.getParentPost());
            p.setChildrenPosts(post.getChildrenPosts());
            p.setTimestamp(post.getTimestamp());
            p.setResources(post.getResources());
            repository.save(p);
        }
    }

    public void deletePost(Integer id){
        //Xóa resource trước
        repository.deleteById(id);
    }

    public List<Post> getAllByFriend(Principal connectUser){
        return repository.findAll();
    }


}
