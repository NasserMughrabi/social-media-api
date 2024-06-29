package com.mughrabi.rest.webservices.restful_web_services.user;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;

@RestController
public class UserResource {

    private UserDaoService service;

    public UserResource(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    @GetMapping(path = "/users/{id}")
    public User retrieveUser(@PathVariable int id) {
       User user = service.findOne(id);
       if (user == null) {
            throw new UserNotFoundException("id: " + id);
       }

       return user;
    }

    // Add links
    // {
    //     "id": 1,
    //     "name": "Adam",
    //     "birthdate": "1994-06-28",
    //     "_links": {
    //       "all-users": {
    //         "href": "http://localhost:8080/users"
    //       }
    //     }
    //   }
    // @GetMapping(path = "/users/{id}")
    // public EntityModel<User> retrieveUser(@PathVariable int id) {
    //    User user = service.findOne(id);
    //    if (user == null) {
    //         throw new UserNotFoundException("id: " + id);
    //    }

    //    EntityModel<User> entityModel = EntityModel.of(user);

       
    //    WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
    //    entityModel.add(link.withRel("all-users"));

    //    return entityModel;
    // }

    @DeleteMapping(path = "/users/{id}")
    public void DeleteUser(@PathVariable int id) {
       service.deleteById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        User savedUser = service.save(user);
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


