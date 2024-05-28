package com.thangkl2420.server_ducky.service;

import com.thangkl2420.server_ducky.dto.post.PostLikeId;
import com.thangkl2420.server_ducky.entity.post.Post;
import com.thangkl2420.server_ducky.entity.post.PostLike;
import com.thangkl2420.server_ducky.entity.user.User;
import com.thangkl2420.server_ducky.repository.FollowingRepository;
import com.thangkl2420.server_ducky.repository.PostLikeRepository;
import com.thangkl2420.server_ducky.repository.PostRepository;
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
    final private FollowingRepository followingRepository;

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

    public Post createPost(Post post, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        post.setUser(user);
        post.setIsComment(0);
        return repository.save(post);
    }

    public Post comment(Post post, Integer parentPostId, Principal connectedUser){
        Post parent = repository.findById(parentPostId).orElse(null);
        if(parent != null){
            var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            post.setUser(user);
            post.setIsComment(1);
            post.setParentPost(parent);
            return repository.save(post);
        }
        return null;
    }

    public Post updatePost(Post post){
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
            return repository.save(p);
        }
        return null;
    }

    public boolean deletePost(Integer id){
        //Xóa resource trước
        try{
            repository.deleteById(id);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public List<Post> getAllPostByIdUser(Integer id){
        return repository.findPostById(id);
    }

    public List<Post> getFollowersPost(Integer id){
        return followingRepository.getAllFollowerPost(id);
    }

    public List<Post> filterPost(long startTime, long endTime, String keyWord){
        return repository.filterPost(startTime, endTime, keyWord);
    }
}
