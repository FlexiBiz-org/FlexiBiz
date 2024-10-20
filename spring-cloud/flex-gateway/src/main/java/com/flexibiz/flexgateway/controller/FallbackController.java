package com.flexibiz.flexgateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallbackController {
    @GetMapping("/{serviceName}")
    public Mono<ResponseEntity<String>> fallBack(@PathVariable String serviceName) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("service " + serviceName + " is unavailable. Please try later."));
    }
}
