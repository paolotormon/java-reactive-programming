package com.dailycodebuffer.reactiveprogramming.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FluxAndMonoServices {

    public Flux<String> fruitsFlux() {
        return Flux.fromIterable(List.of("mango", "orange", "banana")).log();
    }

    public Mono<String> fruitMono() {
        return Mono.just("papaya").log();
    }

    public static void main(String[] args) {

        FluxAndMonoServices fluxAndMonoServices = new FluxAndMonoServices();
        fluxAndMonoServices.fruitsFlux().subscribe(s -> {
            System.out.println("s = " + s);
        });
        fluxAndMonoServices.fruitMono().subscribe(System.out::println);
    }
}
