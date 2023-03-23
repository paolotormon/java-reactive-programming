package com.dailycodebuffer.reactiveprogramming.services;

import com.dailycodebuffer.reactiveprogramming.domain.Book;
import com.dailycodebuffer.reactiveprogramming.domain.BookInfo;
import com.dailycodebuffer.reactiveprogramming.domain.Review;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BookService {

    private BookInfoService bookInfoService;
    private ReviewService reviewService;

    public BookService(BookInfoService bis, ReviewService rs) {
        bookInfoService = bis;
        reviewService = rs;
    }


    public Flux<Book> getBooks() {
        var allBooks = bookInfoService.getBooks();
        return allBooks.flatMap(bookInfo -> {
            Mono<List<Review>> reviews =
                    reviewService.getReviews(bookInfo.getBookId()).collectList();

            return reviews.map(review -> new Book(bookInfo, review)).log();
        });
    }

    public Mono<Book> getBook(Long bookId) {
        var book = bookInfoService.getBook(bookId);
        var review = reviewService.getReviews(bookId).collectList();

//        return book.zipWith(review, (b, r) -> new Book(b, r));
        return book.zipWith(review, Book::new);

    }
}
