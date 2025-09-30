package com.lucasangeloSpring.controllers;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lucasangeloSpring.models.User;
import com.lucasangeloSpring.models.dto.UserCreateDTO;
import com.lucasangeloSpring.models.dto.UserUpdateDTO;
import com.lucasangeloSpring.services.UserService;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    
    @Autowired
    private UserService userService;

    // localhost:8080/user/1
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        User user = this.userService.findById(id);
        return ResponseEntity.ok(user);
    }


@PostMapping
public ResponseEntity<Void> create(@Valid @RequestBody UserCreateDTO obj) { // criar e atualizar pode passar no body
    User user = this.userService.fromDTO(obj);
    User newUser = this.userService.create(user);

    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newUser.getId()) // adiciona o id no path
            .toUri();

    return ResponseEntity.created(uri).build();
}

@PutMapping("/{id}")
public ResponseEntity<Void> update(@Valid @RequestBody UserUpdateDTO obj, @PathVariable Long id) {
    obj.setId(id);
    User user = this.userService.fromDTO(obj);
    this.userService.update(user);
    return ResponseEntity.noContent().build(); // 204 - No content, ou seja, deu certo mas não tem nada para retornar
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id){
    this.userService.delete(id);
    return ResponseEntity.noContent().build();
}

}