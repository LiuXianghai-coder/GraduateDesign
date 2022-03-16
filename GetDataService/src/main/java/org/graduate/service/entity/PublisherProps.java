package org.graduate.service.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 出版社对象的属性信息对象， 包括分页的 Page 对象的大小
 *
 * @author : LiuXianghai on 2020/12/30
 * @Created : 2020/12/30 - 10:38
 * @Project : GetDataService
 */
@Component
@ConfigurationProperties(prefix = "publisher")
@Data
public class PublisherProps {
    // 每次分页的数目大小
    private int pageSize;
}
