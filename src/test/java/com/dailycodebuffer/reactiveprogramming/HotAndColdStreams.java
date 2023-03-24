package com.dailycodebuffer.reactiveprogramming;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class HotAndColdStreams {

    @Test
    public void coldStreamTest() {
        var numbers = Flux.range(1, 10);
        numbers.subscribe(integer -> System.out.println("integer = " + integer));
        numbers.subscribe(integer -> System.out.println("integer = " + integer));
    }

    @Test
    public void hotStreamTest() throws InterruptedException {
        var numbers = Flux.range(1, 10).delayElements(Duration.ofMillis(1000));
//        not working
//        numbers.subscribe(integer -> System.out.println("integer = " + integer));
        ConnectableFlux<Integer> publisher = numbers.publish();
        publisher.connect();
        numbers.subscribe(integer -> System.out.println("subscriber 1 = " + integer));
        Thread.sleep(4000);
        numbers.subscribe(integer -> System.out.println("subscriber 2 = " + integer));
        Thread.sleep(10000);

    }
}
