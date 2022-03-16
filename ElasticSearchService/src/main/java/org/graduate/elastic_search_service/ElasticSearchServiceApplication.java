package org.graduate.elastic_search_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ElasticSearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticSearchServiceApplication.class, args);
    }
}
