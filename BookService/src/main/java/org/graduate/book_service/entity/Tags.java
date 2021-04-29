package org.graduate.book_service.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : LiuXianghai on 2020/12/26
 * @Created : 2020/12/26 - 20:55
 * @Project : GetDataService
 */
@Component
@Data
@ConfigurationProperties(prefix = "getdata.platformlist.platform.tags", ignoreUnknownFields = false)
public class Tags {
    private List<Tag> tags;
}
