package com.flexibiz.flexidentity.controller;

import com.flexibiz.flexidentity.entity.User;
import com.flexibiz.flexidentity.model.LoginParam;
import com.flexibiz.flexidentity.model.RegisterParam;
import com.flexibiz.flexidentity.service.AuthService;
import com.flexibiz.flexidentity.service.JWTService;
import com.flexibiz.flexidentity.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JWTService jwtService;

    @PostMapping("/register")
    public Mono<ResponseEntity<User>> register(@RequestBody RegisterParam userParam) {
        return authService.register(userParam)
                .map(user -> ResponseEntity.created(URI.create("/users/" + user.getId())).body(user))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping("/users/{id}")
    public Mono<ResponseEntity<User>> getUser(@PathVariable Long id) {
        return userService.getUser(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody LoginParam loginParam) {
        return authService.login(loginParam)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping("/roles")
    public Mono<Object> getRoles() {
        String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhY2hyYWZfemFpbSIsInJvbGVzIjpbIlNJTVBMRV9VU0VSIl0sImlhdCI6MTcyNzY0NDY5MCwiZXhwIjoxNzI3NjczNDkwfQ.QcP12BiPtkNir7r_80KmVzq3-xcjv6EK6_MuQhvywmK9XsEUHbRJQCx0F1CfFKwt";
        return Mono.just(jwtService.getRoles(token));
    }
}

