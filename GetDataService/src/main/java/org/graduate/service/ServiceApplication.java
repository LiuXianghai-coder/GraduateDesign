package org.graduate.service;

import org.graduate.service.entity.BasicUrl;
import org.graduate.service.single.BookDetailMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceApplication {

    @Autowired
    public ServiceApplication(BasicUrl basicUrl) {
        new BookDetailMap(basicUrl); // 只能初始化一次！！！！！！！！！！
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}
