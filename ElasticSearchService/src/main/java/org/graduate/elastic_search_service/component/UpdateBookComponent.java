package org.graduate.elastic_search_service.component;

import lombok.extern.slf4j.Slf4j;
import org.graduate.elastic_search_service.service.BookService;
import org.graduate.elastic_search_service.tools.TimeTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @author : LiuXianghai on 2021/3/23
 * @Created : 2021/03/23 - 9:41
 * @Project : elastic_search_service
 */
@Slf4j
@Component
public class UpdateBookComponent {
    private final BookService bookService;

    @Autowired
    public UpdateBookComponent(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * 每三天更新一次作者文档信息
     */
    @Scheduled(fixedDelay = 3*24*60*60*1000)
    public void updateBookDocument() {
        // 更新书籍文档信息
        bookService.updateBookDocument();
        log.info("update Book document: " + TimeTools.currentTime());

        // 更新作者文档信息
        bookService.updateAuthorDocument();
        log.info("update author document: " + TimeTools.currentTime());
    }
}
