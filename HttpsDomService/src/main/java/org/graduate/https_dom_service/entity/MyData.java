package org.graduate.https_dom_service.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 临时数据存储对象， 包含对于数据的临时存储路径， 临时存储的文件名
 *
 * @author : LiuXianghai on 2020/12/28
 * @Created : 2020/12/28 - 11:52
 * @Project : GetDataService
 */
@Data
@Component
@ConfigurationProperties(prefix = "mydata", ignoreUnknownFields = false)
public class MyData {
    // 定义的交换文件的路径
    private String path;

    // 待浏览器执行的 html 文件名
    private String htmlFileName;
}
