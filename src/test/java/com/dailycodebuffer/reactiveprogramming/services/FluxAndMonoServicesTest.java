package com.dailycodebuffer.reactiveprogramming.services;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import java.time.Duration;

class FluxAndMonoServicesTest {
    FluxAndMonoServices fluxAndMonoServices = new FluxAndMonoServices();


    @Test
    void fruitMono() {
        var fruitMono = fluxAndMonoServices.fruitMono();
        StepVerifier.create(fruitMono).expectNext("papaya").verifyComplete();
    }

    @Test
    void fruitMonoFlatMap() {
        var fruitMonoFlatMap = fluxAndMonoServices.fruitMonoFlatMap();
        StepVerifier.create(fruitMonoFlatMap)
                .expectNextCount(1) //[p, a, p , a ...]
                .verifyComplete();
    }

    @Test
    void testFruitMonoFlatMapMany() {
        var fruitsFlux = fluxAndMonoServices.fruitMonoFlatMapMany();
        StepVerifier.create(fruitsFlux)
                .expectNextCount(6)//papaya
                .verifyComplete();
    }

    @Test
    void fruitsFlux() {
        var fruitsFlux = fluxAndMonoServices.fruitsFlux();
        StepVerifier.create(fruitsFlux).expectNext("mango", "orange"
                , "banana").verifyComplete();
    }

    @Test
    void fruitsFluxMap() {
        var fruitsFluxMap = fluxAndMonoServices.fruitsFluxMap();
        StepVerifier.create(fruitsFluxMap).expectNext("MANGO", "ORANGE"
                , "BANANA").verifyComplete();

    }

    @Test
    void fruitsFluxFilter() {
        var fruitsFluxFilter = fluxAndMonoServices.fruitsFluxFilter(5);
        StepVerifier.create(fruitsFluxFilter)
                .expectNext("orange", "banana")
                .verifyComplete();
    }


    @Test
    void fruitsFluxFlatMap() {
        var fruitsFluxFlatMap = fluxAndMonoServices.fruitsFluxFlatMap();
        StepVerifier.create(fruitsFluxFlatMap)
                .expectNextCount(17) //number of letters
                .verifyComplete();
    }

    @Test
    void fruitsFluxFlatMapAsync() {
        var fruitsFluxFlatMapAsync = fluxAndMonoServices.fruitsFluxFlatMapAsync();
        StepVerifier.create(fruitsFluxFlatMapAsync)
                .expectNextCount(17) //number of letters
                .verifyComplete();
    }


    @Test
    void fruitsFluxConcatMap() {
        var fruitsFluxConcatMap = fluxAndMonoServices.fruitsFluxConcatMap();
        StepVerifier.create(fruitsFluxConcatMap)
                .expectNextCount(17) //number of letters
                .verifyComplete();
    }

    @Test
    void fruitsFluxTransform() {

        var fruitsFluxTransform =
                fluxAndMonoServices.fruitsFluxTransform(5);

        StepVerifier.create(fruitsFluxTransform)
                .expectNext("orange", "banana")
                .verifyComplete();
    }

    @Test
    void fruitsFluxTransformHighDefaultIfEmpty() {

        var fruitsFluxTransformIfEmpty =
                fluxAndMonoServices.fruitsFluxTransformIfEmpty(10);

        StepVerifier.create(fruitsFluxTransformIfEmpty)
                .expectNext("Default")
                .verifyComplete();
    }


    @Test
    void fruitsFluxTransformSwitchIfEmpty() {
        var fruitsFluxTransformSwitchIfEmpty =
                fluxAndMonoServices.fruitsFluxTransformSwitchIfEmpty(8);

        StepVerifier.create(fruitsFluxTransformSwitchIfEmpty)
                .expectNext("Pineapple", "Jackfruit")
                .verifyComplete();
    }

    @Test
    void fruitsFluxConcat() {
        var fruits = Flux.just("mango", "orange");
        var veggies = Flux.just("tomato", "lemon");
        var fruitsConcat = fluxAndMonoServices.fruitsFluxConcat(fruits, veggies);
        StepVerifier.create(fruitsConcat)
                .expectNext("mango", "orange", "tomato", "lemon")
                .verifyComplete();
    }

    @Test
    void fruitsFluxConcatWith() {
        var fruits = Flux.just("mango", "orange");
        var veggies = Flux.just("banana", "lemon");
        var fruitsConcat = fluxAndMonoServices.fruitsFluxConcatWith(fruits, veggies);
        StepVerifier.create(fruitsConcat)
                .expectNext("mango", "orange", "banana", "lemon")
                .verifyComplete();
    }

    @Test
    void fruitsFluxMergeWith() {
        var fruits = Flux.just("mango", "orange").delayElements(Duration.ofMillis(50));
        var veggies = Flux.just("lettuce", "cabbage").delayElements(Duration.ofMillis(75));
        var fruitsConcat = fluxAndMonoServices.fruitsFluxMergeWith(fruits, veggies).log();
        StepVerifier.create(fruitsConcat).expectNext("mango", "lettuce", "orange", "cabbage").verifyComplete();
    }

    @Test
    void fruitsFluxMergeSequential() {
        var fruits = Flux.just("mango", "orange").delayElements(Duration.ofMillis(50));
        var veggies = Flux.just("lettuce", "cabbage").delayElements(Duration.ofMillis(75));
        var fruitsConcat = fluxAndMonoServices.fruitsFluxMergeSequential(fruits, veggies).log();
        StepVerifier.create(fruitsConcat).expectNext("mango", "orange", "lettuce", "cabbage").verifyComplete();
    }

    @Test
    void fruitsFluxZip() {
        var fruits = Flux.just("mango", "orange");
        var veggies = Flux.just("lettuce", "cabbage");
        var fruitsConcat = fluxAndMonoServices.fruitsFluxZip(fruits, veggies).log();
        StepVerifier.create(fruitsConcat).expectNext("mangolettuce", "orangecabbage").verifyComplete();
    }

    @Test
    void fruitsFluxZipTuple() {
        var fruits = Flux.just("mango", "orange");
        var veggies = Flux.just("lettuce", "cabbage");
        var food = Flux.just("ramen", "pizza");
        var fruitsConcat = fluxAndMonoServices.fruitsFluxZipTuple(fruits,
                veggies, food).log();
        StepVerifier.create(fruitsConcat).expectNext("mangolettuceramen", "orangecabbagepizza").verifyComplete();
    }

    @Test
    void fruitsFluxFilterDoOn() {
        var fruitsFluxFilter = fluxAndMonoServices.fruitsFluxFilterDoOn(5);

        StepVerifier.create(fruitsFluxFilter)
                .expectNext("orange", "banana")
                .verifyComplete();
    }

    @Test
    void fruitsFluxOnErrorMap() {
        Hooks.onOperatorDebug(); // will slow down app
        var fruitsFlux = fluxAndMonoServices.fruitsFluxOnErrorMap().log();
        StepVerifier.create(fruitsFlux).expectNext("MANGO").expectError(IllegalStateException.class).verify();
    }
}