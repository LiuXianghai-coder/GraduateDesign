package org.graduate.http_dom_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 浏览器的 JS 脚本向服务端发送得到的 DOM 内容，
 * 将会以 json 的格式发送， 因此需要设置对应的类来接受这个对象
 *
 * @author : LiuXianghai on 2021/1/20
 * @Created : 2021/01/20 - 11:18
 * @Project : GetDataService
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class DomObject {
    @JsonProperty("dom")
    private String dom;
}
