package org.graduate.elastic_search_service.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 查找的响应体对象
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/8
 * Time: 上午10:53
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
