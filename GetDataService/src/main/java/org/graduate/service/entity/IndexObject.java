package org.graduate.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 索引信息对象
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/12
 * Time: 下午10:54
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class IndexObject {
    @JsonProperty("index")
    private Index index;
}
