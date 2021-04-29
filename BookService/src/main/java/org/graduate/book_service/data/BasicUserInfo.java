package org.graduate.book_service.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/4
 * Time: 下午7:28
 */

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "user_info", schema = "public", catalog = "recommend_system")
public class BasicUserInfo {
    @Id
    @Column(name = "user_id")
    @JsonProperty("userId")
    private long userId;

    @Basic
    @Column(name = "user_name")
    @JsonProperty("userName")
    private String userName;

    @Basic
    @Column(name = "user_sex")
    @JsonProperty("userSex")
    private String userSex;

    @Basic
    @Column(name = "head_image")
    @JsonProperty("headImage")
    private String headImage;
}
