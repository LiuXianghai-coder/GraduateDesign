package org.graduate.https_dom_service.tools;

import lombok.extern.slf4j.Slf4j;
import org.graduate.https_dom_service.constants.InfoConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 文件的帮助对象类， 定义相关的静态方法完成对文件的一些操作
 *
 * @author : LiuXianghai on 2021/1/20
 * @Created : 2021/01/20 - 10:43
 * @Project : GetDataService
 */
@Slf4j
public final class FileHelper {
    // 日志输出对象
    private final static Logger logger = LoggerFactory.getLogger(FileHelper.class);

    // 默认的文件流的缓冲区大小
    private final static int BUFFER_SIZE = 4096;

    /**
     * 清空一个文件， 这里的做法是通过在原有的文件里面写入一个空字符串来实现的
     *
     * @param file ： 待清空的文件
     */
    public static void cleanFile(File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();

        } catch (Exception e) {
            logger.info(logger.getName() + e.getLocalizedMessage());
        }
    }

    /**
     * 将字符串内容写入到文件中。注意： 这将会清除整个文件
     * @param file ： 待写入的文件对象
     * @param content ： 写入的内容
     * @param bufferSize : 写入文件的缓冲区大小
     * @return ： 写入结果
     */
    public static Boolean writeToFile(File file, String content, int bufferSize) {
        try {
            // 清除文件内容
            cleanFile(file);
            // 获取字符串内容的字节码流
            InputStream inputStream = new ByteArrayInputStream(content.getBytes());
            // 将字节码流输出到文件
            OutputStream outputStream = new FileOutputStream(file);

            // 缓冲区大小
            byte[] bytes = new byte[bufferSize];

            return inputToOutput(inputStream, outputStream, bytes);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将字符串内容写入到文件中。注意： 这将会清除整个文件，
     * 这个方法使用的是默认的缓冲区大小
     * @param file ： 待写入的文件对象
     * @param content ： 写入的内容
     * @return ： 写入结果
     */
    public static Boolean writeToFile(File file, String content) {
        try {
            // 清除文件内容
            cleanFile(file);
            // 获取字符串内容的字节码流
            InputStream inputStream = new ByteArrayInputStream(content.getBytes());
            // 将字节码流输出到文件
            OutputStream outputStream = new FileOutputStream(file);

            // 缓冲区大小
            byte[] bytes = new byte[BUFFER_SIZE];

            return inputToOutput(inputStream, outputStream, bytes);
        } catch (Exception e) {
            logger.info(logger.getName() + file.getName() +
                    InfoConstant.WRITE_FILE_ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * 文件写入的结果显示的对应提示信息
     * 写入正常：无执行结果
     * 写入异常：提示对应的提示信息
     *
     * @param file : 写入的文件
     * @param result: 写入文件的执行结果
     */
    public static void writeToFileResult(File file, boolean result) {
        if (result) return;

        logger.info(logger.getName() + file.getName() +
                InfoConstant.WRITE_FILE_ERROR_MESSAGE);
    }

    /**
     * 将解析到的图书信息链接内容写入临时文件中
     *
     * @param href ： 写入的字符串内容， 在这里是解析得到的图书的列表链接
     * @return ： 写入结果。true ： 写入成功   false ： 写入失败
     */
    public static boolean writeToTempFile(File tempFile, String href, int bufferSize) {
        checkStringParameter(href);

        try {
            // 临时缓冲字节流
            byte[] bytes = new byte[bufferSize];
            // 不断叠加得到的图书的列表
            OutputStream outputStream = new FileOutputStream(tempFile, true);
            InputStream inputStream = new ByteArrayInputStream(href.getBytes());

            return inputToOutput(inputStream, outputStream, bytes);
        } catch (Exception e) {
            logger.info(logger.getName() + " " + InfoConstant.WRITE_STRING_TO_FILE_ERROR);
            return false;
        }
    }

    /**
     * 从文件对象中读取对应的内容
     * @param file ： 待读取的文件对象
     * @return ： 读取到的内容
     * @throws IOException ： 读取文件时存在异常则抛出
     */
    public static String readFromFile(File file) throws IOException {
        StringBuilder result = new StringBuilder();

        int len;
        byte[] buffer = new byte[BUFFER_SIZE];
        InputStream in      =   new FileInputStream(file);

        while ((len = in.read(buffer)) > 0) {
            String s = new String(buffer, 0, len);
            result.append(s);
        }

        return result.toString();
    }

    /**
     * 将输入流写入到输出流对象中
     * @param in ： 输入流对象
     * @param out ： 输出流对象
     * @param bytes ： 缓冲区大小
     * @return ： 写入结果
     */
    private static boolean inputToOutput(InputStream in, OutputStream out, byte[] bytes) {
        int len;
        try {
            // 获取输入字节码， 输出到文件中
            while ((len = in.read(bytes)) > 0) {
                out.write(bytes, 0, len);
            }
            // 将输出流数据冲洗到文件内
            out.flush();
            // 关闭输出流对象
            out.close();
            // 关闭输入流对象
            in.close();

            return true;
        } catch (Exception e) {
            logger.info(logger.getName() + " " + InfoConstant.WRITE_STRING_TO_FILE_ERROR);
            return false;
        }
    }

    /**
     * 检测输入的字符串参数是否有效
     * 如果传入的字符串参数为空或者有效长度为 0， 则抛出 IllegalArgumentException
     */
    private static void checkStringParameter(String parameter) {
        if (null == parameter || 0 == parameter.trim().length()) {
            throw new IllegalArgumentException(logger.getName() + " "
                    + InfoConstant.STRING_PARAMETER_INVALID_MESSAGE);
        }
    }
}
