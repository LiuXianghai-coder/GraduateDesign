package org.graduate.service.constants;

/**
 * 定义的一些基本常量, 如 URL 参数： http://www.jd.com/search?keyWord=***&page=1
 *
 * @author : LiuXianghai on 2020/12/30
 * @Created : 2020/12/30 - 12:22
 * @Project : GetDataService
 */
public final class Const {
    // 添加的 URL 参数的名称
    public final static String PAGE_PARAMETER_KEY = "page";

    // 方法名的字符串量
    public final static String METHOD_STRING = " method: ";

    // 默认是否使用正则表达式获取数据内容的标记， 默认为不启用
    public final static Boolean DEFAULT_USE_REGEX_FLAG = Boolean.FALSE;

    // 默认的获取信息列表的索引位置
    public final static Integer DEFAULT_GET_INFO_INDEX = 0;

    // 图书的默认评分
    public final static Short DEFAULT_BOOK_SCORE = 0;

    // 默认的主题名称
    public final static String TOPIC_NAME = "Parse-Dom";

    // Kafka 消费者的组ID
    public final static String GROUP_ID = "test-consumer-group";

    // 由于 application.yml 不支持中文, 所以不得已， 只能手动在代码中设置对应的正则表达式了
    // 出版时间的正则表达式模式
    public final static String PUBLISHER_DATE_REGEX = "(\\d\\d\\d\\d-\\d\\d-\\d\\d)";

    // 出版社名称的正则表达式匹配模式
    public final static String PUBLISHER_NAME_REGEX = "title=\"([^\"]+)\"";

    public final static int THRESHOLD = 10; // 轮询最大阈值
}
