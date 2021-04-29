package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/2
 * Time: 下午3:44
 */

@Data
public class UserBook {
    @JsonProperty("star")
    private Boolean star;

    @JsonProperty("comment")
    private Boolean comment;

    @JsonProperty("collection")
    private Boolean collection;
}
