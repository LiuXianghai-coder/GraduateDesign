package org.graduate.book_service.component;

import lombok.extern.slf4j.Slf4j;
import org.graduate.book_service.tools.SaveBookUrl;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author : LiuXianghai on 2021/3/27
 * @Created : 2021/03/27 - 14:12
 * @Project : BookService
 */
@Slf4j
@Component
public class ScheduleUpdateComponent {
    private final SaveBookUrl saveBookUrl;

    public ScheduleUpdateComponent(SaveBookUrl saveBookUrl) {
        this.saveBookUrl = saveBookUrl;
    }

    // @Scheduled(fixedDelay = 30 * 24 * 60 * 60L)
    public void updateBookUrls() throws IOException, InterruptedException {
        if (saveBookUrl.updateAllBookUrls() == HttpStatus.OK) {
            log.info("Update Book Urls Successful");
            return;
        }

        log.info("Update Book Urls Failed");
    }
}
