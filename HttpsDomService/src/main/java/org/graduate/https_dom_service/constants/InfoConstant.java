package org.graduate.https_dom_service.constants;

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
}
