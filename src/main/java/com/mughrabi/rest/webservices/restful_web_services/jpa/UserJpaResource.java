package com.mughrabi.rest.webservices.restful_web_services.jpa;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mughrabi.rest.webservices.restful_web_services.user.Post;
import com.mughrabi.rest.webservices.restful_web_services.user.User;
import com.mughrabi.rest.webservices.restful_web_services.user.UserNotFoundException;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {

    private UserRepository userRepository;
    private PostRepository postRepository;

    public UserJpaResource(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/jpa/users/{id}")
    public Optional<User> retrieveUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("id: " + id);
        }

        return user;
    }

    @DeleteMapping(path = "/jpa/users/{id}")
    public void DeleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
    }

    @GetMapping(path = "/jpa/users/{id}/posts")
    public List<Post> retrievePostsForUser(@PathVariable int id) {
        // Find the user
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("id: " + id);
        }

        // Get the posts
        return user.get().getPosts();
    }

    @PostMapping(path = "/jpa/users/{id}/posts")
    public ResponseEntity<Object> createPostForUser(@PathVariable int id, @Valid @RequestBody Post post) {
        // Find the user
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("id: " + id);
        }

        post.setUser(user.get());

        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest() // /user
                .path("/{id}") // /{id}
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/jpa/users/{id}/posts/{post_id}")
    public Optional<Post> retrievePostForUser(@PathVariable int id, @PathVariable int post_id) {
        // Find the user - make sure user exists
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("id: " + id);
        }

        // Find the post - make sure post exists and belongs to user with give id
        Optional<Post> post = postRepository.findById(post_id);
        if (post.isEmpty() || !post.get().getUser().getId().equals(id)) {
            // throw new PostNotFoundException("post_id: " + post_id + " for user_id: " + id); // implement this
            throw new UserNotFoundException("id: " + id);
        }
        return post;
    }

    @PostMapping("/jpa/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);
        // location - /user/{id}
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest() // /user
                .path("/{id}") // /{id}
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

}

// 200 success
// 201 created
// 204 no content
// 400 bad request
// 401 unauthorized
// 404 resource not found
// 500 server error
