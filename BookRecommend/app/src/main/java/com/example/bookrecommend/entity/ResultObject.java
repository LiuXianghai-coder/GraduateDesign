package com.example.bookrecommend.entity;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通过访问相关搜索接口得到的响应体对象类。
 * 用于获取相关的搜索结果的信息。如：响应时间、是否超时等
 *
 * @author: LiuXianghai
 * @Time: 2021年2月20日22:30:55
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ResultObject<T> {
    /*
        响应时间
     */
    private Long took;

    /*
        是否超时
     */
    private Boolean timeOut;

    /*
        总共的分片数量
     */
    private Integer totalShards;

    /*
        成功的分片数量
     */
    private Integer successfulShards;

    /*
        失败的分片次数
     */
    private Integer failedShards;

    /*
        查找到的相关记录的数量
     */
    private Long recordNum;

    /*
        查找的方式
     */
    private String relation;

    /*
        查找到的对象列表
     */
    private List<T> objectList;
}
