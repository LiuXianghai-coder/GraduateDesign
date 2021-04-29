package org.graduate.service.entity;

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

    // 抓取数据时的临时存放数据的名称
    private String elementsFileName;

    // 临时存储信息的文件
    private String tempFileName;

    // 字符的编码规则
    private String charsetName;

    // 缓冲字节流的缓冲区的大小
    private Integer bufferSize;

    // 控制浏览器加载数据的脚本文件名
    private String scriptFileName;

    // 待浏览器执行的 html 文件名
    private String htmlFileName;
}
