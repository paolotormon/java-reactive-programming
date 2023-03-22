package com.dailycodebuffer.reactiveprogramming.services;

import com.dailycodebuffer.reactiveprogramming.domain.BookInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookInfoService {

    static Map<Long, BookInfo> bookMap = new HashMap<>();

    static {
        bookMap.put(1L, new BookInfo(1, "Book One", "Author One", "12121212"));
        bookMap.put(2L, new BookInfo(2, "Book Two", "Author Two", "12312312"));
        bookMap.put(3L, new BookInfo(3, "Book Three", "Author Three", "12412412"));
    }

    public Flux<BookInfo> getBooks() {
        List bookList = new ArrayList<BookInfo>(bookMap.values());
        return Flux.fromIterable(bookList);
    }

    public Mono<BookInfo> getBook(long bookId) {
        var book = bookMap.get(bookId);
        return Mono.just(book);
    }
}
