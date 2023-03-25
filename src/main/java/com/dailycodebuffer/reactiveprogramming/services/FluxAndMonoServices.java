package com.dailycodebuffer.reactiveprogramming.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.tools.agent.ReactorDebugAgent;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
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
//        Also possible syntax with `just()`
//        return Flux.just("mango", "orange", "banana")
//                .filter(s -> s.length() > number);
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
    }

    public Flux<String> fruitsFluxTransformIfEmpty(int number) {

        Function<Flux<String>, Flux<String>> filterData =
                data -> data.filter(s -> s.length() > number);

        return Flux.fromIterable(List.of("mango", "orange", "banana"))
                .transform(filterData)
                .defaultIfEmpty("Default")
                .log();
    }

    public Flux<String> fruitsFluxTransformSwitchIfEmpty(int number) {

        Function<Flux<String>, Flux<String>> filterData =
                data -> data.filter(s -> s.length() > number);

        return Flux.fromIterable(List.of("mango", "orange", "banana"))
                .transform(filterData)
                .switchIfEmpty(Flux.just("Pineapple", "Jackfruit")
                        .transform(filterData))
                .log();
    }

    public Flux<String> fruitsFluxConcat(Flux<String> flux1, Flux<String> flux2) {
        return Flux.concat(flux1, flux2);
    }

    public Flux<String> fruitsFluxConcatWith(Flux<String> flux1, Flux<String> flux2) {
        return flux1.concatWith(flux2);
    }

    //    concat but async
    public Flux<String> fruitsFluxMergeWith(Flux<String> flux1, Flux<String> flux2) {
        return flux1.mergeWith(flux2);
    }

    public Flux<String> fruitsFluxMergeSequential(Flux<String> flux1, Flux<String> flux2) {
        return Flux.mergeSequential(flux1, flux2);
    }

    public Flux<String> fruitsFluxZip(Flux<String> flux1, Flux<String> flux2) {
        return Flux.zip(flux1, flux2, (first, second) -> first + second);
//        return flux1.zip(flux1, flux2, (first, second) -> first + second);
    }

    public Flux<String> fruitsFluxZipTuple(Flux<String> flux1,
                                           Flux<String> flux2,
                                           Flux<String> flux3) {
        return Flux.zip(flux1, flux2, flux3).map(objects -> objects.getT1() + objects.getT2() + objects.getT3());
    }

    public Flux<String> fruitsFluxFilterDoOn(int number) {
        return Flux.fromIterable(List.of("mango", "orange", "banana"))
                .filter(s -> s.length() > number)
                .doOnNext(s -> System.out.println("s = " + s))
                .doOnSubscribe(
                        subscription -> System.out.println("subscription.toString()" +
                                " = " + subscription)
                ).doOnComplete(() -> System.out.println("Completed! "));
    }

    public Flux<String> fruitsFluxOnErrorMap() {
        ReactorDebugAgent.init();
        ReactorDebugAgent.processExistingClasses();
        return Flux.fromIterable(List.of("mango", "orange", "banana"))
//                .checkpoint("Error checkpoint 1")
                .map(s -> {
                    if (s.equalsIgnoreCase("orange"))
                        throw new RuntimeException();
                    return s.toUpperCase();
                })
//                .checkpoint("Error checkpoint 2")
                .onErrorMap(throwable -> {
                    System.out.println("throwable = " + throwable);
                    return new IllegalStateException("From onErrorMap");
                });
    }

    public static void main(String[] args) {

        FluxAndMonoServices fluxAndMonoServices = new FluxAndMonoServices();
        fluxAndMonoServices.fruitsFlux().subscribe(s -> {
            System.out.println("s = " + s);
        });
        fluxAndMonoServices.fruitMono().subscribe(System.out::println);
    }
}
