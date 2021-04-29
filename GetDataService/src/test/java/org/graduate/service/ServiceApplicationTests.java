package org.graduate.service;

import org.graduate.service.constants.UserAgent;
import org.graduate.service.data_entity.BookUrl;
import org.graduate.service.entity.BasicUrl;
import org.graduate.service.entity.BookDetail;
import org.graduate.service.entity.MyConfiguration;
import org.graduate.service.repository.BookUrlRepo;
import org.graduate.service.single.BookDetailMap;
import org.graduate.service.tool.GetData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class ServiceApplicationTests {
    @Autowired
    private GetData getData;

    @Autowired
    private BasicUrl basicUrl;

    @Autowired
    private MyConfiguration myConfiguration;

    @Autowired
    private BookUrlRepo bookUrlRepo;

    @Test
    void contextLoads() throws IOException, InterruptedException {
        int count = 0;
        File file = new File("/tmp/data.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        List<BookUrl> bookUrlList = bookUrlRepo.findBookUrlsByStartAndEnd(0L, 100L);
        for (BookUrl url: bookUrlList) {
            Document document = Jsoup.connect(url.getUrl())
                    .userAgent(UserAgent.randomUserAgent()).get();
            writer.write("Count: " + ++count + "\n"
                    + document.getAllElements().toString() + "\n\n");
        }

        writer.close();
    }
}
