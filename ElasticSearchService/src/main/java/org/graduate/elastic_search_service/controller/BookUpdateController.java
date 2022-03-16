package org.graduate.elastic_search_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.graduate.elastic_search_service.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : LiuXianghai on 2021/3/9
 * @Created : 2021/03/09 - 21:47
 * @Project : elastic_search_service
 */
@Slf4j
@RestController
@RequestMapping(path = "/updateDocument")
public class BookUpdateController {
    private final BookService bookService;

    @Autowired
    public BookUpdateController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(path = "/updateBookDocument")
    public HttpStatus updateBookDocument() {
        if (!bookService.updateBookDocument()) {
            return HttpStatus.valueOf("Update Book Document failed.");
        }

        return HttpStatus.OK;
    }

    @GetMapping(path = "/updateAuthorDocument")
    public HttpStatus updateAuthorDocument() {
        if (!bookService.updateAuthorDocument()) {
            return HttpStatus.valueOf("Update Author Document failed");
        }

        return HttpStatus.OK;
    }
}
