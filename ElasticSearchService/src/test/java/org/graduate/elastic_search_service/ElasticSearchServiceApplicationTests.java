package org.graduate.elastic_search_service;

import org.graduate.elastic_search_service.data.AuthorBook;
import org.graduate.elastic_search_service.data.Book;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = {
                org.graduate.elastic_search_service.data.AuthorBook.class,
                org.graduate.elastic_search_service.data.Book.class
        }
)
class ElasticSearchServiceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testField(){
        Field[] fields = Book.class.getDeclaredFields();
        for (Field field: fields) {
            System.out.println(field.getName() + ": " + field.getType());
        }
    }
}
