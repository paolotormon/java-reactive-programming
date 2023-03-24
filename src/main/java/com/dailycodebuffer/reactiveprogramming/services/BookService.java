package com.dailycodebuffer.reactiveprogramming.services;

import com.dailycodebuffer.reactiveprogramming.domain.Book;
import com.dailycodebuffer.reactiveprogramming.domain.Review;
import com.dailycodebuffer.reactiveprogramming.exception.BookException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.List;

@Slf4j
public class BookService {

    private BookInfoService bookInfoService;
    private ReviewService reviewService;

    public BookService(BookInfoService bis, ReviewService rs) {
        bookInfoService = bis;
        reviewService = rs;
    }


    public Flux<Book> getBooks() {
        var allBooks = bookInfoService.getBooksInfo();
        return allBooks.flatMap(bookInfo -> {
                    Mono<List<Review>> reviews =
                            reviewService.getReviews(bookInfo.getBookId()).collectList();

                    return reviews.map(review -> new Book(bookInfo, review)).log();
                })
                .onErrorMap(throwable -> {
                    log.error("Excetption: " + throwable);
                    return new BookException("Exception occured while " +
                            "fetching books");
                })
                .log();
    }

    public Flux<Book> getBooksRetry() {
        var allBooks = bookInfoService.getBooksInfo();
        return allBooks.flatMap(bookInfo -> {
                    Mono<List<Review>> reviews =
                            reviewService.getReviews(bookInfo.getBookId()).collectList();

                    return reviews.map(review -> new Book(bookInfo, review)).log();
                })
                .onErrorMap(throwable -> {
                    log.error("Excetption: " + throwable);
                    return new BookException("Exception occured while " +
                            "fetching books");
                })
                .retry(3) // total of 4 tries
                .log();
    }

    public Flux<Book> getBooksRetryWhen() {
//        RetryBackoffSpec retrySpecs = getRetryBackoffSpec();
        var allBooks = bookInfoService.getBooksInfo();
        return allBooks.flatMap(bookInfo -> {
                    Mono<List<Review>> reviews =
                            reviewService.getReviews(bookInfo.getBookId()).collectList();

                    return reviews.map(review -> new Book(bookInfo, review)).log();
                })
                .onErrorMap(throwable -> {
                    log.error("Excetption: " + throwable);
                    return new BookException("Exception occured while " +
                            "fetching books");
                })
                .retryWhen(getRetryBackoffSpec())
                .log();
    }

    private RetryBackoffSpec getRetryBackoffSpec() {
        var retrySpecs = Retry
                .backoff(3, Duration.ofMillis(1000))
                .filter(throwable -> throwable instanceof BookException)
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                        Exceptions.propagate(retrySignal.failure())
                );
        return retrySpecs;
    }

    public Mono<Book> getBook(Long bookId) {
        var book = bookInfoService.getBookInfo(bookId);
        var review = reviewService.getReviews(bookId).collectList();

//        return book.zipWith(review, (b, r) -> new Book(b, r));
        return book.zipWith(review, Book::new);

    }
}
