package com.vivy.springdoc.scoped.startertest.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/test")
public class StarterApi {

    @GetMapping
    public Mono<ResponseEntity<?>> helloTest() {
        return Mono.just(ResponseEntity.ok().build());
    }
}
