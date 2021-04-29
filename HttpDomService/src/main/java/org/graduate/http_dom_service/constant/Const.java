package org.graduate.http_dom_service.constant;

/**
 * 定义的一些基本常量, 如 URL 参数： http://www.jd.com/search?keyWord=***&page=1
 *
 * @author : LiuXianghai on 2020/12/30
 * @Created : 2020/12/30 - 12:22
 * @Project : GetDataService
 */
public final class Const {
    // 默认的主题名称
    public final static String TOPIC_NAME = "Graduate-Http";

    // 处理 Https 协议的主题名称
    public final static String HTTPS_TOPIC_NAME = "Graduate-Https";

    // Kafka 消费者的组ID
    public final static String GROUP_ID = "test-consumer-group";

    // http 头信息
    public final static String HTTP_HEAD_INFO = "http";

    // 更新 dom 处理状态的访问地址
    public final static String ACCESS_UPDATE_FLAG_ADDR =
            "https://127.0.0.1:8080/access/updateFinishedFlag";
}
