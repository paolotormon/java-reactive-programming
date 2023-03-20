package com.dailycodebuffer.reactiveprogramming.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FluxAndMonoServices {

    public Flux<String> fruitsFlux() {
        return Flux.fromIterable(List.of("mango", "orange", "banana")).log();
    }

    public Mono<String> fruitMono() {
        return Mono.just("papaya")
                .log();
    }

    public Flux<String> fruitsFluxMap() {
        return Flux.fromIterable(List.of("mango", "orange", "banana"))
                .map(String::toUpperCase);
    }

    public Flux<String> fruitsFluxFilter(int number) {
        return Flux.fromIterable(List.of("mango", "orange", "banana"))
                .filter(s -> s.length() > number);
    }

    public Flux<String> fruitsFluxFlatMap(int number) {
        return Flux.fromIterable(List.of("mango", "orange", "banana"))
                .flatMap(s->Flux.just(s.split("")))
                .log();
    }

    public static void main(String[] args) {

        FluxAndMonoServices fluxAndMonoServices = new FluxAndMonoServices();
        fluxAndMonoServices.fruitsFlux().subscribe(s -> {
            System.out.println("s = " + s);
        });
        fluxAndMonoServices.fruitMono().subscribe(System.out::println);
    }
}
