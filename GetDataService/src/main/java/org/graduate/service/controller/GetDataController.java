
package org.graduate.service.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.graduate.service.data_entity.BookUrl;
import org.graduate.service.entity.BookDetail;
import org.graduate.service.repository.BookUrlRepo;
import org.graduate.service.single.BookDetailMap;
import org.graduate.service.tool.GetData;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping(path = "/")
@Import(org.graduate.service.tool.GetData.class)
public class GetDataController {
    private final GetData getData;

    private final BookUrlRepo bookUrlRepo;

    private final Random random = new Random();

    private final static Map<Short, BookDetail> bookDetailMap = BookDetailMap.getBookDetailMap();

    @Autowired
    public GetDataController(GetData getData, BookUrlRepo bookUrlRepo) {
        this.getData = getData;
        this.bookUrlRepo = bookUrlRepo;
    }

    public HttpStatus getData() throws IOException, InterruptedException {
        getData.parseBookList();

        return HttpStatus.OK;
    }

    @SneakyThrows
    @GetMapping(path = "/parseContent/{start}/{size}")
    public HttpStatus parseContent(@PathVariable Long start,
                                   @PathVariable Long size) {
        List<BookUrl> bookUrlList = bookUrlRepo.findBookUrlsByStartAndEnd(start, size);
        for (BookUrl url: bookUrlList) {
            log.info("url: " + url.toString());
            getData.saveBookInfo(url.getUrl(), bookDetailMap.get(url.getHashBookDetail()));
            log.info("SaveBookInfo Success.........");

            TimeUnit.SECONDS.sleep(random.nextInt(10));
        }

        return HttpStatus.OK;
    }

    @GetMapping(path = "/savePublishers")
    public HttpStatus savePublishers() throws Exception {
        getData.savePublisher();

        return HttpStatus.OK;
    }

    @GetMapping(path = "/test")
    public HttpStatus test() throws Exception {
        Document document = getData.getDocumentFromElementFile();

        return HttpStatus.OK;
    }
}
