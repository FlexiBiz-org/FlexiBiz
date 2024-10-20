package com.flexibiz.flexidentity.service;

import com.flexibiz.flexidentity.entity.User;
import com.flexibiz.flexidentity.entity.UserAuthority;
import com.flexibiz.flexidentity.model.LoginParam;
import com.flexibiz.flexidentity.model.RegisterParam;
import com.flexibiz.flexidentity.repository.UserAuthorityRepository;
import com.flexibiz.flexidentity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReactiveAuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public Mono<User> register(RegisterParam userParams) {
        return userRepository.findByUsername(userParams.getUsername())
                .flatMap(existingUser -> Mono.error(new RuntimeException("Username already exists")))
                .switchIfEmpty(Mono.defer(() -> {
                    User user = new User();
                    user.setUsername(userParams.getUsername());
                    user.setPassword(passwordEncoder.encode(userParams.getPassword()));

                    return userRepository.save(user).flatMap(savedUser -> {

                        List<UserAuthority> authorities = List.of(new UserAuthority("SIMPLE_USER", savedUser.getId()));

                        return userAuthorityRepository.saveAll(authorities)
                                .then(Mono.just(savedUser));
                    });
                })).cast(User.class);
    }

    public Mono<String> login(LoginParam loginParam) {
        return Mono.just(new UsernamePasswordAuthenticationToken(loginParam.getUsername(), loginParam.getPassword()))
                .flatMap(authenticationManager::authenticate)
                .filter(Authentication::isAuthenticated)
                .map(authentication -> {
                    List<String> roles = authentication.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList();
                    return jwtService.generateToken(loginParam.getUsername(), roles);
                });
    }
}
