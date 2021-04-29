package org.graduate.service.tools;

import lombok.Data;
import org.graduate.service.constant.InfoConstant;
import org.graduate.service.entity.MyConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 产生 DOM 文档对象的帮助工具
 *
 * @author : LiuXianghai on 2021/1/4
 * @Created : 2021/01/04 - 20:39
 * @Project : GetDataService
 */
@Data
public class ParseDom {
    // 生成消息的对象
    private final Logger logger = LoggerFactory.getLogger(ParseDom.class);

    // 自定义的配置属性对象， 需要通过构造参数传入
    private final MyConfiguration myConfiguration;

    public ParseDom(MyConfiguration myConfiguration) {
        this.myConfiguration = myConfiguration;
    }

    /**
     * 检测传入的字符串参数是否是有效的
     * @param parameter ： 待检测的字符串参数
     */
    private void checkStringParameter(String parameter) {
        // 当前执行的方法名
        String methodName = new Object(){}.getClass()
                .getEnclosingMethod().getName();

        // 如果对象为空或者有效长度为 0， 则是无效的参数
        if (null == parameter || 0 == parameter.trim().length()) {
            logger.info(logger.getName() + " " + methodName + " " + InfoConstant.PARAMETER_VALID_INFO);
            throw new IllegalArgumentException(InfoConstant.PARAMETER_VALID_INFO);
        }
    }

    /**
     * 检测请求的地址是否是值得信赖的， 这里使用的是简单的暴力匹配算法，
     * 这不是一种很好的做法， 但是在这里是值得的。
     * 相比较使用 红黑树 或者 哈希表， 可能在数据量比较大的时候将会有比较快的查找速度，
     * 但是这里的实际情况是：最多包含两个可信赖的主机 IP 地址。
     * 所以使用较复杂的数据结构可能会适得其反
     *
     * @param addr ： 待验证的 IP 地址
     * @return ： 是否是值得信赖的 IP 地址
     */
    public Boolean isTrustable(String addr){
        // 检测传入的参数是否是有效的
        checkStringParameter(addr);

        List<String> address = myConfiguration.getTrustableRemoteAddr();
        for (String host: address) {
            if (addr.equalsIgnoreCase(host)) return true;
        }

        return false;
    }

    /**
     * 得到完善的文件执行路径。
     * 在有的浏览器中，如果不加入对应的协议信息， 将无法运行对应的文件， 如 Firefox 84.0.1 (64 位)
     * 因此需要完善路径信息
     * @param href ： 待完善的路径信息, 应当是文件的路径信息， 如： ~/hello.html
     * @return ： 完善后的路径信息 如：file://~/hello.html
     */
    public String perfectHref(String href) {
        checkStringParameter(href);

        // 检测链接是否已经完善的链接地址的正则表达式对象
        Pattern pattern = Pattern.compile(myConfiguration.getFileProtocolRegex());
        Matcher matcher = pattern.matcher(href);

        // 如果满足要求， 则直接返回原有链接对象
        if (matcher.find()) return href;

        return myConfiguration.getFileProtocolPrefix() + href;
    }

    /**
     * 整合所有的命令为一个整体命令
     * @param commands ： 待整合的命令列表
     * @param splitChar : 分隔符
     * @return ： 整合后的命令结果
     */
    public String combineCommand(String[] commands, String splitChar) {
        if (null == commands || 0 == commands.length) return null;

        StringBuilder command = new StringBuilder();
        // 默认以空白字符作为分隔符
        for (String string: commands) {
            command.append(string).append(splitChar);
        }
        // 移除最后一个分隔符
        command.deleteCharAt(command.length() - 1);

        return command.toString();
    }
}
