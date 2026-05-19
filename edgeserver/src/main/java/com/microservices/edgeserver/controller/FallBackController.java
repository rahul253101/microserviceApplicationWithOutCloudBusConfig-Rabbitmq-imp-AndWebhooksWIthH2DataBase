package com.microservices.edgeserver.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallBackController {
    @RequestMapping("/contactSupport")
    public Mono<String> contactSupport(){
        return Mono.just("Our service is down currently please try after sometime or contact dev team");
    }
}
