package org.graduate.usershare.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author : LiuXianghai on 2021/3/9
 * @Created : 2021/03/09 - 22:40
 * @Project : elastic_search_service
 */
@Configuration
public class PageableConfiguration {
    @Bean(name = "defaultPage")
    public Pageable defaultPage() {
        return PageRequest.of(0, 20, Sort.by("comment_num"));
    }
}
