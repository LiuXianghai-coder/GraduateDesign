package org.graduate.book_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
*  Created by IntelliJ IDEA.
*  User: liuxianghai
*  Date: 2021/2/12
*  Time: 下午10:53
*/

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Index {
    // 索引 Id
    @JsonProperty("_id")
    private long id;
}
