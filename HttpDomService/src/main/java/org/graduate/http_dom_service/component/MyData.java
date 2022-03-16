package org.graduate.http_dom_service.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/24
 * Time: 上午9:47
 */
@Data
@Component
@ConfigurationProperties(prefix = "mydata", ignoreUnknownFields = false)
public class MyData {
    // 定义的交换文件的路径
    private String path;

    // 抓取数据时的临时存放数据的名称
    private String elementsFileName;

    // 缓冲字节流的缓冲区的大小
    private Integer bufferSize;

    // 处理中的 html 文件名
    private String htmlFileName;

    // 浏览器显示的页面文件名称， 对应 templates 路径下的 thymeleaf 名称
    private String pageFileName;
}
