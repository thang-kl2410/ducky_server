package com.thangkl2420.server_ducky.service;

import com.thangkl2420.server_ducky.dto.FilterRequest;
import com.thangkl2420.server_ducky.dto.post.PostLikeId;
import com.thangkl2420.server_ducky.entity.post.Post;
import com.thangkl2420.server_ducky.entity.post.PostLike;
import com.thangkl2420.server_ducky.entity.user.User;
import com.thangkl2420.server_ducky.repository.FollowingRepository;
import com.thangkl2420.server_ducky.repository.PostLikeRepository;
import com.thangkl2420.server_ducky.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    final private PostRepository repository;
    final private PostLikeRepository postLikeRepository;
    final private FollowingRepository followingRepository;

    public Page<Post> getAll(FilterRequest request){
        Pageable pageable = PageRequest.of(request.getPageIndex(),request.getPageSize());
        return repository.filterPost(request.getStartTime(), request.getEndTime(), request.getKeyword(), pageable);
    }

    public List<Post> getPostByUser(Integer id, Integer pageIndex, Integer pageSize){
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return repository.findPostById(id, pageable).getContent();
    }

    public List<Post> getComments(Integer idPost, Integer pageIndex, Integer pageSize){
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return repository.findCommentPost(idPost, pageable).getContent();
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
            p.setContent(post.getContent());
            p.setChildrenPosts(post.getChildrenPosts());
            p.setImages(post.getImages());
            p.setIsComment(0);
            return repository.save(p);
        }
        return null;
    }

    @Transactional
    public boolean deletePost(Integer id){
//        Post _post = repository.findById(id).orElse(new Post());
        try{
//            repository.deleteById(id);
//            _post.getImages().clear();
//            repository.save(_post);
            repository.deleteCommentPostById(id);
            repository.deletePostById(id);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public List<Post> getAllPostByIdUser(Integer id, Integer pageIndex, Integer pageSize){
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return repository.findPostById(id, pageable).getContent();
    }

    public List<Post> getFollowersPost(Principal connectedUser, Integer pageIndex, Integer pageSize){
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return followingRepository.getAllFollowerPost(user.getId(), pageable).getContent();
    }
}
