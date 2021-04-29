package org.graduate.usershare.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : LiuXianghai on 2021/3/9
 * @Created : 2021/03/09 - 17:21
 * @Project : usershare
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserShareContent {
    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("shareContent")
    private String shareContent;
}
