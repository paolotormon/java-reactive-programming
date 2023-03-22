package com.dailycodebuffer.reactiveprogramming.services;

import com.dailycodebuffer.reactiveprogramming.domain.BookInfo;
import com.dailycodebuffer.reactiveprogramming.domain.Review;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewService {

    static Map<Long, Review> reviewMap = new HashMap();

    static {
        reviewMap.put(1L, new Review(1L, 1L, 100, "WEW"));
        reviewMap.put(2L, new Review(2L, 2L, 55, "a"));
        reviewMap.put(3L, new Review(3L, 3L, 70, "d"));
        reviewMap.put(4L, new Review(4L, 1L, 88, "hahahahaha"));
    }

    public Flux<Review> getReviews(Long bookID) {
        List<Review> reviewsList = new ArrayList<Review>(reviewMap.values());
        return Flux.fromIterable(reviewsList).filter(review -> review.getBookId() == bookID);

    }

    public Mono<Review> getReview(Long reviewId) {
        return Mono.just(reviewMap.get(reviewId));
    }
}
