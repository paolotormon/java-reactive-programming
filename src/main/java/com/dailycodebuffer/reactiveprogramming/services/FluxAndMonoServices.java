package com.dailycodebuffer.reactiveprogramming.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoServices {

    public Mono<String> fruitMono() {
        return Mono.just("papaya")
                .log();
    }

    public Flux<String> fruitMonoFlatMapMany() {
        return Mono.just("papaya")
                .flatMapMany(s -> Flux.just(s.split("")))
                .log();
    }

    public Mono<List<String>> fruitMonoFlatMap() {
        return Mono.just("papaya")
                .flatMap(s -> Mono.just(List.of(s.split(""))))
                .log();
    }

    public Flux<String> fruitsFlux() {
        return Flux.fromIterable(List.of("mango", "orange", "banana")).log();
    }

    public Flux<String> fruitsFluxMap() {
        return Flux.fromIterable(List.of("mango", "orange", "banana"))
                .map(String::toUpperCase);
    }

    public Flux<String> fruitsFluxFilter(int number) {
        return Flux.fromIterable(List.of("mango", "orange", "banana"))
                .filter(s -> s.length() > number);
    }

    public Flux<String> fruitsFluxFlatMap() {
        return Flux.fromIterable(List.of("mango", "orange", "banana"))
                .flatMap(s -> Flux.just(s.split("")))
                .log();
    }

    public Flux<String> fruitsFluxFlatMapAsync() {
        return Flux.fromIterable(List.of("mango", "orange", "banana"))
                .flatMap(s -> Flux.just(s.split(""))
                        .delayElements(Duration.ofMillis(
                                new Random().nextInt(1000)
                        )))
                .log();
    }

    //  concat map is  same as flatmap but preserves order of async
    public Flux<String> fruitsFluxConcatMap() {
        return Flux.fromIterable(List.of("mango", "orange", "banana"))
                .concatMap(s -> Flux.just(s.split(""))
                        .delayElements(Duration.ofMillis(
                                new Random().nextInt(1000)
                        )))
                .log();
    }

    public Flux<String> fruitsFluxTransform(int number) {

        Function<Flux<String>, Flux<String>> filterData =
                data -> data.filter(s -> s.length() > number);

        return Flux.fromIterable(List.of("mango", "orange", "banana"))
                .transform(filterData)
                .log();
//                .filter(s -> s.length() > number);
    }

    public static void main(String[] args) {

        FluxAndMonoServices fluxAndMonoServices = new FluxAndMonoServices();
        fluxAndMonoServices.fruitsFlux().subscribe(s -> {
            System.out.println("s = " + s);
        });
        fluxAndMonoServices.fruitMono().subscribe(System.out::println);
    }
}
