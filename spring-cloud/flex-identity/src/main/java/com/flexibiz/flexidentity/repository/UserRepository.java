package com.flexibiz.flexidentity.repository;

import com.flexibiz.flexidentity.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> findByUsername(String username);
    Mono<User> findById(Long id);
}

