package org.graduate.book_service.entity;

import lombok.Data;

/**
 * 当前爬取页面的头部信息， 包括访问页面的 http 协议等
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/23
 * Time: 下午5:33
 */
@Data
public class HeadInfo {
    // http 协议
    private String httpProtocol;
}
