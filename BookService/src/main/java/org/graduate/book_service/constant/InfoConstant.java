package org.graduate.book_service.constant;

/**
 * 定义一些基本的常量信息， 如：infoMessage 用于输出对应的通知信息
 *
 * @author : LiuXianghai on 2020/12/25
 * @Created : 2020/12/25 - 21:27
 * @Project : GetDataService
 */
public final class InfoConstant {
    // 参数异常时的输出信息，在参数为 null 或者有效长度为 0 的情况下。
    public final static String PARAMETER_VALID_INFO = " parameter is null or valid length is 0.";

    // 标签选择器的格式错误信息， 格式应当为 {tag}@{selectName}
    public final static String SELECT_NAME_FORMAT_INVALID =
            " selectName format invalid, please check your " +
            "getdata.platformList.platform.getElementList.getElement.selectName value in application.yml file";

    // 文件写入异常时的提示信息
    public final static String WRITE_FILE_ERROR_MESSAGE =  " write to file error";

    // 文件创建成功的提示信息
    public final static String CREATE_FILE_SUCCESS = "create file success";

    // 删除文件成功的信息
    public final static String DELETE_FILE_SUCCESS = " delete file success";

    // 清空一个文件， 处理成功的提示信息
    public final static String CLEAR_FILE_CONTENT_SUCCESS = "delete file content success";

    // 字符串参数验证失败出现的提示信息
    public final static String STRING_PARAMETER_INVALID_MESSAGE = " string parameter valid length is 0.";

    // 将字符串内容写入文件出现异常时的提示信息
    public final static String WRITE_STRING_TO_FILE_ERROR = " write string content to file error.";

    // 文件不存在， 将会创建这个文件的提示信息
    public final static String FILE_NOT_EXIST_MESSAGE = " the file not exist, will create it.";

    // 解析图书信息出现异常时的提示信息
    public final static String PARSE_BOOK_INFO_FAILED_MESSAGE = " parse bookInfo from href failed.";

    // 链接格式出现异常时的提示信息
    public final static String HREF_FORMAT_ERROR_MESSAGE = " href format invalid, Please check it.";

    // 参数的对象为 null 的提示信息
    public final static String OBJECT_PARAMETER_IS_NULL_MESSAGE = " the object parameter is null";

    // 开始解析 Dom 的信息内容
    public final static String START_PARSE_DOM_MESSAGE = "Please start to parse Dom";

    // 完成 Dom 解析的信息内容
    public final static String FINISHED_DOM_PARSE_MESSAGE = "The Dom has been parsed";

    // 格式化 HTML 文件成功的提示信息
    public final static String FORMAT_HTML_SUCCESS = "format html file success";

    // 格式化 HTML 文件失败的提示信息
    public final static String FORMAT_HTML_FAILED = "format html file failed";

    // 将 Kafka 消息发送成功时的提示信息
    public final static String KAFKA_SEND_SUCCESS_Message = " send StartParseDomMessage successful! message: ";

    // 将Kafka 消息发送失败时的提示信息
    public final static String KAFKA_SEND_FIALED_Message = "send StartParseDomMessage successful! message:";

    // 将浏览器解析到的 Dom 文档写入到文件中成功时的提示信息
    public final static String PARSE_DOM_TO_FILE_SUCCESS_MESSAGE = " parsed dom content write to html file success.";

    // 将浏览器解析到的 Dom 写入到文件中失败时的提示信息
    public final static String PARSE_DOM_TO_FILE_FAILED_MESSAGE = " parsed dom content write to html file failed.";

    // 将 JS 脚本文件写入 html 文件成功的提示信息
    public final static String APPEND_JS_SCRIPT_SUCCESS = "append JS script to html success.";

    // 书籍详细信息对象为空时的提示信息
    public final static String BOOK_DETAIL_IS_NULL = "BookDetail Object is null.";

    // 保存 BookInfo 对象成功时的提示信息
    public final static String SAVE_BOOK_INFO_SUCCESS = "save BookInfo object success";

    // 保存 BookInfo 对象失败时的提示信息
    public final static String SAVE_BOOK_INFO_FAILED = "save BookInfo object success";
}
