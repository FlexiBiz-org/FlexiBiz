package com.flexibiz.flexidentity.service;

import com.flexibiz.flexidentity.entity.User;
import com.flexibiz.flexidentity.model.UserWithRoles;
import com.flexibiz.flexidentity.repository.UserAuthorityRepository;
import com.flexibiz.flexidentity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;

    public Mono<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("username : " + username + " not found")))
                .flatMap(user -> userAuthorityRepository.findByUserId(user.getId())
                        .collectList()
                        .map(authorities -> new UserWithRoles(user, authorities))
                )
                .cast(UserDetails.class);
    }
}
